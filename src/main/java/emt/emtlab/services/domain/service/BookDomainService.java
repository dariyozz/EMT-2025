package emt.emtlab.services.domain.service;

import emt.emtlab.services.application.dto.book.BookDto;
import emt.emtlab.services.domain.model.Book;
import emt.emtlab.services.domain.model.BookRental;
import emt.emtlab.services.domain.model.Category;
import emt.emtlab.services.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface BookDomainService {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    Optional<Book> update(Book book, BookDto bookDto);

    void deleteById(Long id);

    Optional<Book> markAsRented(Book book, User user);

    Optional<Book> returnBook(Book book, User user);

    List<BookRental> getAllActiveRentals();

    List<BookRental> getUserRentals(User user);

    List<Book> filterBooks(String name, Long authorId, Category category);

    Optional<Book> markAsReturned(Book book);

    List<Book> findRecentBooks();
}