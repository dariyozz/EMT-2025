package emt.emtlab.services.domain.service.impl;

import emt.emtlab.services.domain.model.Author;
import emt.emtlab.services.domain.repository.AuthorRepository;
import emt.emtlab.services.domain.service.AuthorDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorDomainServiceImpl implements AuthorDomainService {

    private final AuthorRepository authorRepository;

    public AuthorDomainServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
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
        return authorRepository.save(author);
    }

    @Override
    public Optional<Author> update(Long id, Author authorDetails) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(authorDetails.getName());
                    author.setSurname(authorDetails.getSurname());
                    author.setCountry(authorDetails.getCountry());
                    return authorRepository.save(author);
                });
    }

    @Override
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public List<Author> findByNameContaining(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }
}