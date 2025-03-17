package emt.emtlab.model.dto;

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
) {}