package emt.emtlab.model.dto;

import emt.emtlab.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookDto(
        Long id,
        @NotBlank(message = "Book name cannot be blank")
        String name,
        @NotNull(message = "Category is required")
        Category category,
        @NotNull(message = "Author is required")
        Long authorId,
        @Min(value = 0, message = "Available copies must be non-negative")
        int availableCopies,
        boolean rented,
        boolean deleted
) {
}