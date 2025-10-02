package emt.emtlab.mapper;

import emt.emtlab.services.domain.model.BookRental;
import emt.emtlab.services.application.dto.book.BookRentalDto;
import org.springframework.stereotype.Component;

@Component
public class BookRentalMapper {

    public BookRentalDto toDto(BookRental bookRental) {
        return new BookRentalDto(
            bookRental.getId(),
            bookRental.getBook().getId(),
            bookRental.getBook().getName(),
            bookRental.getUser().getId(),
            bookRental.getUser().getUsername(),
            bookRental.getRentalDate(),
            bookRental.getReturnDate()
        );
    }
}