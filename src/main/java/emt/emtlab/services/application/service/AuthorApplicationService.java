package emt.emtlab.services.application.service;

import emt.emtlab.services.application.dto.author.AuthorDto;
import java.util.List;
import java.util.Optional;

public interface AuthorApplicationService {
    List<AuthorDto> findAll();
    Optional<AuthorDto> findById(Long id);
    AuthorDto save(AuthorDto authorDto);
    Optional<AuthorDto> update(Long id, AuthorDto authorDto);
    void deleteById(Long id);
    List<AuthorDto> findByNameContaining(String name);
}