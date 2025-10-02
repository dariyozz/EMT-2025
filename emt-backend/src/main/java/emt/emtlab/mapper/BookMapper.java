package emt.emtlab.mapper;

import emt.emtlab.services.application.dto.book.BookDto;
import emt.emtlab.services.domain.model.Book;
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
                book.isRented(),
                book.isDeleted(),
                book.getCreatedAt()
        );
    }


}