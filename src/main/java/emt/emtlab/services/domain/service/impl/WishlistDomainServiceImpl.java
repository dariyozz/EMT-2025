package emt.emtlab.services.domain.service.impl;

import emt.emtlab.exceptions.ResourceNotFoundException;
import emt.emtlab.services.domain.model.Book;
import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.model.Wishlist;
import emt.emtlab.services.domain.repository.BookRepository;
import emt.emtlab.services.domain.repository.UserRepository;
import emt.emtlab.services.domain.repository.WishlistRepository;
import emt.emtlab.services.domain.service.BookDomainService;
import emt.emtlab.services.domain.service.WishlistDomainService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistDomainServiceImpl implements WishlistDomainService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookDomainService bookDomainService;

    public WishlistDomainServiceImpl(
            WishlistRepository wishlistRepository,
            UserRepository userRepository,
            BookRepository bookRepository,
            BookDomainService bookDomainService) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookDomainService = bookDomainService;
    }

    @Override
    public Wishlist getOrCreateWishlist(User user) {
        return wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist(user);
                    return wishlistRepository.save(newWishlist);
                });
    }

    @Override
    @Transactional
    public Wishlist addBookToWishlist(User user, Book book) {
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("This book has no available copies to add to wishlist");
        }

        Wishlist wishlist = getOrCreateWishlist(user);

        // Check if book is already in wishlist
        if (wishlist.getBooks().contains(book)) {
            throw new IllegalStateException("Book is already in the wishlist");
        }

        wishlist.addBook(book);
        return wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional
    public Wishlist removeBookFromWishlist(User user, Book book) {
        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for user"));

        wishlist.removeBook(book);
        return wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional
    public List<Book> rentAllBooks(User user, Wishlist wishlist) {
        List<Book> booksToRent = wishlist.getBooks();
        if (booksToRent.isEmpty()) {
            throw new IllegalStateException("Wishlist is empty");
        }

        List<Book> rentedBooks = new ArrayList<>();
        for (Book book : booksToRent) {
            if (book.getAvailableCopies() > 0) {
                bookDomainService.markAsRented(book, user)
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
    public void clearWishlist(Wishlist wishlist) {
        wishlist.setBooks(new ArrayList<>());
        wishlistRepository.save(wishlist);
    }

    // New methods that work with IDs
    @Override
    public Wishlist getOrCreateWishlistByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return getOrCreateWishlist(user);
    }

    @Override
    @Transactional
    public Wishlist addBookToWishlistByIds(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        return addBookToWishlist(user, book);
    }

    @Override
    @Transactional
    public Wishlist removeBookFromWishlistByIds(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        return removeBookFromWishlist(user, book);
    }

    @Override
    @Transactional
    public List<Book> rentAllBooksFromWishlistByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Wishlist wishlist = getOrCreateWishlist(user);
        return rentAllBooks(user, wishlist);
    }

    @Override
    @Transactional
    public void clearWishlistByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Wishlist wishlist = getOrCreateWishlist(user);
        clearWishlist(wishlist);
    }
}