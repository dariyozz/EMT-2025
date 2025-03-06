package emt.emtlab.mapper;

import emt.emtlab.model.dto.AuthorDto;
import emt.emtlab.model.Author;
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
}