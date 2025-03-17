package emt.emtlab.service.Impl;

import emt.emtlab.exceptions.ResourceNotFoundException;
import emt.emtlab.mapper.BookMapper;
import emt.emtlab.mapper.BookRentalMapper;
import emt.emtlab.model.*;
import emt.emtlab.model.dto.BookDto;
import emt.emtlab.model.dto.BookRentalDto;
import emt.emtlab.repository.AuthorRepository;
import emt.emtlab.repository.BookRentalRepository;
import emt.emtlab.repository.BookRepository;
import emt.emtlab.repository.UserRepository;
import emt.emtlab.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final BookRentalRepository bookRentalRepository;
    private final UserRepository userRepository;
    private final BookRentalMapper bookRentalMapper;

    // Update the constructor with new dependencies
    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           BookMapper bookMapper,
                           BookRentalRepository bookRentalRepository,
                           UserRepository userRepository,
                           BookRentalMapper bookRentalMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
        this.bookRentalRepository = bookRentalRepository;
        this.userRepository = userRepository;
        this.bookRentalMapper = bookRentalMapper;
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .filter(bookDto -> !bookDto.deleted())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDto> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto).filter(bookDto -> !bookDto.deleted());
    }

    @Override
    @Transactional
    public BookDto save(BookDto bookDto) {
        Author author = authorRepository.findById(bookDto.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + bookDto.authorId()));

        Book book = new Book();
        book.setName(bookDto.name());
        book.setCategory(bookDto.category());
        book.setAuthor(author);
        book.setAvailableCopies(bookDto.availableCopies());
        book.setRented(bookDto.rented());

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public Optional<BookDto> update(Long id, BookDto bookDto) {
        return bookRepository.findById(id)
                .map(book -> {
                    Author author = authorRepository.findById(bookDto.authorId())
                            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + bookDto.authorId()));

                    book.setName(bookDto.name());
                    book.setCategory(bookDto.category());
                    book.setAuthor(author);
                    book.setAvailableCopies(bookDto.availableCopies());
                    book.setRented(bookDto.rented());

                    return bookMapper.toDto(bookRepository.save(book));
                });
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<BookDto> bookDto = this.findById(id);
        if (bookDto.isPresent())
            bookRepository.findById(id).get().setDeleted(true);
    }

    @Override
    @Transactional
    public Optional<BookDto> markAsRented(Long bookId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return bookRepository.findById(bookId)
                .map(book -> {
                    // Check if book is already rented
                    //TODO allow book to be rented as long as there are copies. (DELETE THIS)

                    Optional<BookRental> existingRental = bookRentalRepository.findByBookAndReturnDateIsNull(book);
                    if (existingRental.isPresent()) {
                        throw new IllegalStateException("Book is already rented");
                    }

                    // Check if book has available copies
                    if (book.getAvailableCopies() > 0) {
                        book.setAvailableCopies(book.getAvailableCopies() - 1);
                        if (book.getAvailableCopies() == 0) {
                            book.setRented(true);
                        }

                        // Create rental record
                        BookRental rental = new BookRental(book, user);
                        bookRentalRepository.save(rental);

                        return bookMapper.toDto(bookRepository.save(book));
                    }
                    throw new IllegalStateException("No available copies to rent");
                });
    }

    @Override
    @Transactional
    public Optional<BookDto> returnBook(Long bookId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        BookRental rental = bookRentalRepository.findByBookAndReturnDateIsNull(book)
                .orElseThrow(() -> new IllegalStateException("Book is not currently rented"));

        // Verify this user is the one who rented the book
        if (!rental.getUser().getId().equals(userId)) {
            throw new IllegalStateException("This book was rented by a different user");
        }

        // Mark as returned
        rental.setReturnDate(LocalDateTime.now());
        bookRentalRepository.save(rental);

        // Update book availability
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        book.setRented(false);

        return Optional.of(bookMapper.toDto(bookRepository.save(book)));
    }

    @Override
    public List<BookRentalDto> getAllActiveRentals() {
        return bookRentalRepository.findByReturnDateIsNull().stream()
                .map(bookRentalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookRentalDto> getUserRentals(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return bookRentalRepository.findByUserAndReturnDateIsNull(user).stream()
                .map(bookRentalMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<Category> getAllCategories() {
        return Arrays.asList(Category.values());
    }

    // Add to BookServiceImpl.java
    @Override
    public List<BookDto> filterBooks(String name, Long authorId, String category) {
        Category categoryEnum = null;
        if (category != null && !category.isEmpty()) {
            try {
                categoryEnum = Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid category provided, will be treated as null
            }
        }

        return bookRepository.findBooksByFilters(name, authorId, categoryEnum)
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }


}