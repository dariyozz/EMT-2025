package emt.emtlab.services.application.dto.book;

import emt.emtlab.services.domain.model.BookRental;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookRentalDto(
        Long id,
        @NotNull Long bookId,
        String bookName,
        @NotNull Long userId,
        String username,
        LocalDateTime rentalDate,
        LocalDateTime returnDate
) {
    public static BookRentalDto from(BookRental bookRental) {
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