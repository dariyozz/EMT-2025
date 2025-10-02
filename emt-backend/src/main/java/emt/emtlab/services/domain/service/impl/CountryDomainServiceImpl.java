package emt.emtlab.services.domain.service.impl;

import emt.emtlab.services.application.dto.country.CountryDto;
import emt.emtlab.services.domain.model.Country;
import emt.emtlab.services.domain.repository.CountryRepository;
import emt.emtlab.services.domain.service.CountryDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryDomainServiceImpl implements CountryDomainService {

    private final CountryRepository countryRepository;

    public CountryDomainServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> findById(Long id) {
        return countryRepository.findById(id);
    }

    @Override
    public Country save(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Optional<Country> update(Country countryDetails, CountryDto countryDto) {
        countryDetails.setName(countryDto.getName());
        countryDetails.setContinent(countryDto.getContinent());
        return Optional.of(countryRepository.save(countryDetails));
    }

    @Override
    public void deleteById(Long id) {
        countryRepository.deleteById(id);
    }

    @Override
    public List<Country> findByNameContaining(String name) {
        return countryRepository.findByNameContainingIgnoreCase(name);
    }
}