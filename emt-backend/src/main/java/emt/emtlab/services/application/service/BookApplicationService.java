package emt.emtlab.services.application.service;

import emt.emtlab.services.application.dto.book.BookDto;
import emt.emtlab.services.application.dto.book.BookRentalDto;
import emt.emtlab.services.domain.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BookApplicationService {
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

    List<BookDto> findRecentBooks();

    Page<BookDto> findAll(int page, int size, String sortBy, String direction);
}