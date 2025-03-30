package emt.emtlab.services.application.dto.country;

import jakarta.validation.constraints.NotBlank;

public record CountryDto(
        Long id,
        @NotBlank(message = "Country name cannot be blank")
        String name,
        @NotBlank(message = "Continent cannot be blank")
        String continent
) {
}