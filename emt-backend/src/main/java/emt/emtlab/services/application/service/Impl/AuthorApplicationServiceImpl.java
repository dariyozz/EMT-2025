package emt.emtlab.services.application.service.Impl;

import emt.emtlab.exceptions.ResourceNotFoundException;
import emt.emtlab.mapper.AuthorMapper;
import emt.emtlab.services.application.dto.author.AuthorDto;
import emt.emtlab.services.application.service.AuthorApplicationService;
import emt.emtlab.services.domain.model.Author;
import emt.emtlab.services.domain.model.Country;
import emt.emtlab.services.domain.repository.AuthorRepository;
import emt.emtlab.services.domain.service.AuthorDomainService;
import emt.emtlab.services.domain.service.CountryDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorApplicationServiceImpl implements AuthorApplicationService {

    private final AuthorDomainService authorDomainService;
    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;
    private final CountryDomainService countryDomainService;

    public AuthorApplicationServiceImpl(AuthorDomainService authorDomainService, AuthorMapper authorMapper, AuthorRepository authorRepository, CountryDomainService countryDomainService) {
        this.authorDomainService = authorDomainService;
        this.authorMapper = authorMapper;
        this.authorRepository = authorRepository;
        this.countryDomainService = countryDomainService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> findAll() {
        return authorDomainService.findAll().stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorDto> findById(Long id) {
        return authorDomainService.findById(id)
                .map(authorMapper::toDto);
    }

    @Override
    public AuthorDto save(AuthorDto authorDto) {
        Author author = authorMapper.toEntity(authorDto);
        Country country = countryDomainService.findById(authorDto.countryId()).orElseThrow(() -> new ResourceNotFoundException("No such country !"));
        author.setCountry(country);
        return authorMapper.toDto(authorDomainService.save(author));
    }

    @Override
    public Optional<AuthorDto> update(Long id, AuthorDto authorDto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        Country country = countryDomainService.findById(authorDto.countryId()).orElseThrow(() ->
                new ResourceNotFoundException("No such country !"));
        author.setCountry(country);
        return authorDomainService.update(author, authorDto)
                .map(authorMapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        authorDomainService.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> findByNameContaining(String name) {
        return authorDomainService.findByNameContaining(name).stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    public Page<AuthorDto> findAll(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toLowerCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return authorRepository.findAll(pageable).map(authorMapper::toDto);
    }
}
