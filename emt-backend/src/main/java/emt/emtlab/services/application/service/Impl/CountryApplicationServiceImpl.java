package emt.emtlab.services.application.service.Impl;

import emt.emtlab.mapper.CountryMapper;
import emt.emtlab.services.application.dto.country.CountryDto;
import emt.emtlab.services.application.service.CountryApplicationService;
import emt.emtlab.services.domain.model.Country;
import emt.emtlab.services.domain.repository.CountryRepository;
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
public class CountryApplicationServiceImpl implements CountryApplicationService {

    private final CountryDomainService countryDomainService;
    private final CountryMapper countryMapper;
    private final CountryRepository countryRepository;

    public CountryApplicationServiceImpl(CountryDomainService countryDomainService, CountryMapper countryMapper, CountryRepository countryRepository) {
        this.countryDomainService = countryDomainService;
        this.countryMapper = countryMapper;
        this.countryRepository = countryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryDto> findAll() {
        return countryDomainService.findAll().stream()
                .map(countryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CountryDto> findById(Long id) {
        return countryDomainService.findById(id)
                .map(countryMapper::toDto);
    }

    @Override
    public CountryDto save(CountryDto countryDto) {
        Country country = countryMapper.toEntity(countryDto);
        return countryMapper.toDto(countryDomainService.save(country));
    }

    @Override
    public Optional<CountryDto> update(Long id, CountryDto countryDto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));
        return countryDomainService.update(country, countryDto)
                .map(countryMapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        countryDomainService.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryDto> findByNameContaining(String name) {
        return countryDomainService.findByNameContaining(name).stream()
                .map(countryMapper::toDto)
                .toList();
    }

    @Override
    public Page<CountryDto> findAll(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toLowerCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return countryRepository.findAll(pageable).map(countryMapper::toDto);
    }
}