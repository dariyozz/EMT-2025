package emt.emtlab.services.domain.service.impl;

import emt.emtlab.services.domain.model.Book;
import emt.emtlab.services.domain.model.BookRental;
import emt.emtlab.services.domain.model.Category;
import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.repository.BookRentalRepository;
import emt.emtlab.services.domain.repository.BookRepository;
import emt.emtlab.services.domain.service.BookDomainService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookDomainServiceImpl implements BookDomainService {

    private final BookRepository bookRepository;
    private final BookRentalRepository bookRentalRepository;

    public BookDomainServiceImpl(BookRepository bookRepository,
                                BookRentalRepository bookRentalRepository) {
        this.bookRepository = bookRepository;
        this.bookRentalRepository = bookRentalRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll().stream()
                .filter(book -> !book.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id).filter(book -> !book.isDeleted());
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> update(Long id, Book updatedBook) {
        return bookRepository.findById(id).map(existingBook -> {
            existingBook.setName(updatedBook.getName());
            existingBook.setCategory(updatedBook.getCategory());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setAvailableCopies(updatedBook.getAvailableCopies());
            existingBook.setRented(updatedBook.isRented());
            return bookRepository.save(existingBook);
        });
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setDeleted(true);
            bookRepository.save(book);
        });
    }

    @Override
    public Optional<Book> markAsRented(Book book, User user) {
        Optional<BookRental> existingRental = bookRentalRepository.findByBookAndReturnDateIsNull(book);

        if (existingRental.isPresent()) {
            throw new IllegalStateException("Book is already rented");
        }

        if (book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            if (book.getAvailableCopies() == 0) {
                book.setRented(true);
            }

            BookRental rental = new BookRental(book, user);
            bookRentalRepository.save(rental);

            return Optional.of(bookRepository.save(book));
        }

        throw new IllegalStateException("No available copies to rent");
    }

    @Override
    public Optional<Book> returnBook(Book book, User user) {
        Optional<BookRental> existingRental = bookRentalRepository.findByBookAndReturnDateIsNull(book);

        if (existingRental.isPresent() && existingRental.get().getUser().equals(user)) {
            existingRental.get().setReturnDate(LocalDateTime.now());
            bookRentalRepository.save(existingRental.get());

            book.setAvailableCopies(book.getAvailableCopies() + 1);
            book.setRented(false);

            return Optional.of(bookRepository.save(book));
        }

        throw new IllegalStateException("Book is not rented by the user");
    }

    @Override
    public List<BookRental> getAllActiveRentals() {
        return bookRentalRepository.findByReturnDateIsNull();
    }

    @Override
    public List<BookRental> getUserRentals(User user) {
        return bookRentalRepository.findByUserAndReturnDateIsNull(user);
    }

    @Override
    public List<Book> filterBooks(String name, Long authorId, Category category) {
        return bookRepository.findBooksByFilters(name, authorId, category);
    }

    @Override
    public Optional<Book> markAsReturned(Book book) {
        Optional<BookRental> existingRental = bookRentalRepository.findByBookAndReturnDateIsNull(book);

        if (existingRental.isPresent()) {
            existingRental.get().setReturnDate(LocalDateTime.now());
            bookRentalRepository.save(existingRental.get());

            book.setAvailableCopies(book.getAvailableCopies() + 1);
            book.setRented(false);

            return Optional.of(bookRepository.save(book));
        }

        throw new IllegalStateException("Book is not rented");
    }


}