package emt.emtlab.mapper;

import emt.emtlab.model.BookRental;
import emt.emtlab.model.dto.BookRentalDto;
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