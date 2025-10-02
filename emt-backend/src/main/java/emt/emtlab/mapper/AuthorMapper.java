package emt.emtlab.mapper;

import emt.emtlab.services.application.dto.author.AuthorDto;
import emt.emtlab.services.domain.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public AuthorDto toDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getName(),
                author.getSurname(),
                author.getCountry() != null ? author.getCountry().getId() : null
        );
    }

    public Author toEntity(AuthorDto authorDto) {
        return new Author(
                authorDto.name(),
                authorDto.surname()
        );
    }
}