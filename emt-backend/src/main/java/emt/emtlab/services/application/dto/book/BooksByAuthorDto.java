package emt.emtlab.services.application.dto.book;

public record BooksByAuthorDto(
    Long authorId,
    String authorName,
    String authorSurname,
    Long bookCount
) {}