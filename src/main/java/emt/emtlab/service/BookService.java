package emt.emtlab.service;

import emt.emtlab.model.dto.BookDto;
import emt.emtlab.model.Category;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookDto> findAll();

    Optional<BookDto> findById(Long id);

    BookDto save(BookDto bookDto);

    Optional<BookDto> update(Long id, BookDto bookDto);

    void deleteById(Long id);

    Optional<BookDto> markAsRented(Long id);

    List<Category> getAllCategories();
}