package emt.emtlab.services.application.dto.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public record AuthorDto(
        @Getter
        Long id,
        @NotBlank(message = "Author name cannot be blank")
        @Getter
        String name,
        @Getter
        @NotBlank(message = "Author surname cannot be blank")
        String surname,
        @Getter
        @NotNull(message = "Country is required")
        Long countryId
) {

}