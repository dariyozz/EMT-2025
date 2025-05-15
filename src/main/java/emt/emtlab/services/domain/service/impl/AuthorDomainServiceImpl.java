package emt.emtlab.services.domain.service.impl;

import emt.emtlab.events.AuthorChangeEvent;
import emt.emtlab.services.application.dto.author.AuthorDto;
import emt.emtlab.services.domain.model.Author;
import emt.emtlab.services.domain.repository.AuthorRepository;
import emt.emtlab.services.domain.service.AuthorDomainService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorDomainServiceImpl implements AuthorDomainService {

    private final AuthorRepository authorRepository;
    private final ApplicationEventPublisher eventPublisher;


    public AuthorDomainServiceImpl(AuthorRepository authorRepository, ApplicationEventPublisher eventPublisher) {
        this.authorRepository = authorRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public Author save(Author author) {
        Author saved = authorRepository.save(author);
        eventPublisher.publishEvent(new AuthorChangeEvent(this, saved, "SAVE"));
        return saved;
    }

    @Override
    public Optional<Author> update(Author authorDetails, AuthorDto authorDto) {
        authorDetails.setName(authorDto.getName());
        authorDetails.setSurname(authorDto.getSurname());
        return Optional.of(authorRepository.save(authorDetails));
    }

    @Override
    public void deleteById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        authorRepository.delete(author);
        eventPublisher.publishEvent(new AuthorChangeEvent(this, author, "DELETE"));
    }

    @Override
    public List<Author> findByNameContaining(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }
}