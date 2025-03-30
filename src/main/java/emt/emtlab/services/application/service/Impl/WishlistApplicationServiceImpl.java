package emt.emtlab.services.application.service.Impl;

import emt.emtlab.exceptions.ResourceNotFoundException;
import emt.emtlab.mapper.WishlistMapper;
import emt.emtlab.services.application.dto.book.BookDto;
import emt.emtlab.services.application.dto.wishlist.WishlistDto;
import emt.emtlab.services.application.service.BookApplicationService;
import emt.emtlab.services.application.service.WishlistApplicationService;
import emt.emtlab.services.domain.model.Book;
import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.model.Wishlist;
import emt.emtlab.services.domain.repository.BookRepository;
import emt.emtlab.services.domain.repository.UserRepository;
import emt.emtlab.services.domain.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistApplicationServiceImpl implements WishlistApplicationService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookApplicationService bookService;
    private final WishlistMapper wishlistMapper;

    @Autowired
    public WishlistApplicationServiceImpl(
            WishlistRepository wishlistRepository,
            UserRepository userRepository,
            BookRepository bookRepository,
            BookApplicationService bookService,
            WishlistMapper wishlistMapper) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
        this.wishlistMapper = wishlistMapper;
    }

    @Override
    public WishlistDto getOrCreateWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist(user);
                    return wishlistRepository.save(newWishlist);
                });

        return wishlistMapper.toDto(wishlist);
    }

    @Override
    @Transactional
    public WishlistDto addBookToWishlist(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("This book has no available copies to add to wishlist");
        }

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist(user);
                    return wishlistRepository.save(newWishlist);
                });

        // Check if book is already in wishlist
        if (wishlist.getBooks().contains(book)) {
            throw new IllegalStateException("Book is already in the wishlist");
        }

        wishlist.addBook(book);
        wishlist = wishlistRepository.save(wishlist);

        return wishlistMapper.toDto(wishlist);
    }

    @Override
    @Transactional
    public WishlistDto removeBookFromWishlist(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for user id: " + userId));

        wishlist.removeBook(book);
        wishlist = wishlistRepository.save(wishlist);

        return wishlistMapper.toDto(wishlist);
    }

    @Override
    @Transactional
    public List<BookDto> rentAllBooksFromWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for user id: " + userId));

        List<Book> booksToRent = wishlist.getBooks();
        if (booksToRent.isEmpty()) {
            throw new IllegalStateException("Wishlist is empty");
        }

        List<BookDto> rentedBooks = new ArrayList<>();
        for (Book book : booksToRent) {
            if (book.getAvailableCopies() > 0) {
                bookService.markAsRented(book.getId(), userId)
                        .ifPresent(rentedBooks::add);
            }
        }

        // Clear the wishlist after renting
        wishlist.setBooks(new ArrayList<>());
        wishlistRepository.save(wishlist);

        return rentedBooks;
    }

    @Override
    @Transactional
    public void clearWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for user id: " + userId));

        wishlist.setBooks(new ArrayList<>());
        wishlistRepository.save(wishlist);
    }
}