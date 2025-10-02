package emt.emtlab.services.domain.service;

import emt.emtlab.services.application.dto.author.AuthorDto;
import emt.emtlab.services.domain.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDomainService {
    List<Author> findAll();

    Optional<Author> findById(Long id);

    Author save(Author author);

    Optional<Author> update(Author authorDetails, AuthorDto authorDto);

    void deleteById(Long id);

    List<Author> findByNameContaining(String name);
}