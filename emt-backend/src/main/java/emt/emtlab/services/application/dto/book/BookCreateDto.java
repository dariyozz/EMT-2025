package emt.emtlab.services.application.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookCreateDto(
    @NotBlank(message = "Name is required")
    String name,

    @NotNull(message = "Category is required")
    String category,

    @NotNull(message = "Author is required")
    Long authorId,

    @Min(value = 0, message = "Available copies must be non-negative")
    Integer availableCopies
) {}