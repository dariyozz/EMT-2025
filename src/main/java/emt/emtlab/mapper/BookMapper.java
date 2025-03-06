package emt.emtlab.mapper;

import emt.emtlab.model.dto.BookDto;
import emt.emtlab.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getName(),
                book.getCategory(),
                book.getAuthor() != null ? book.getAuthor().getId() : null,
                book.getAvailableCopies(),
                book.isRented()
        );
    }
}