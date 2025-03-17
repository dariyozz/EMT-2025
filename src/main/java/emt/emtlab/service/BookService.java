package emt.emtlab.service;

import emt.emtlab.model.Category;
import emt.emtlab.model.dto.BookDto;
import emt.emtlab.model.dto.BookRentalDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookDto> findAll();

    Optional<BookDto> findById(Long id);

    BookDto save(BookDto bookDto);

    Optional<BookDto> update(Long id, BookDto bookDto);

    void deleteById(Long id);

    Optional<BookDto> markAsRented(Long bookId, Long userId);

    Optional<BookDto> returnBook(Long bookId, Long userId);

    List<BookRentalDto> getAllActiveRentals();

    List<BookRentalDto> getUserRentals(Long userId);

    List<Category> getAllCategories();

    List<BookDto> filterBooks(String name, Long authorId, String category);
}