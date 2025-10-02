package emt.emtlab.services.application.dto.author;

public record AuthorsByCountryDto(
        Long countryId,
        String countryName,
        String continent,
        Long authorCount
) {}