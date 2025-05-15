package emt.emtlab.services.application.dto.country;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record CountryDto(
        @Getter
        Long id,
        @Getter
        @NotBlank(message = "Country name cannot be blank")
        String name,
        @Getter
        @NotBlank(message = "Continent cannot be blank")
        String continent
) {
}