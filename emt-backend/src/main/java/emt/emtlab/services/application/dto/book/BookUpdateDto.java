package emt.emtlab.services.application.dto.book;

import jakarta.validation.constraints.Min;

public record BookUpdateDto(
    String name,
    String category,
    Long authorId,
    @Min(value = 0, message = "Available copies must be non-negative")
    Integer availableCopies
) {}