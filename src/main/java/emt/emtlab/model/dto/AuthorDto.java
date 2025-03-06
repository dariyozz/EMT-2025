package emt.emtlab.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthorDto(
        Long id,
        @NotBlank(message = "Author name cannot be blank")
        String name,
        @NotBlank(message = "Author surname cannot be blank")
        String surname,
        @NotNull(message = "Country is required")
        Long countryId
) {
}