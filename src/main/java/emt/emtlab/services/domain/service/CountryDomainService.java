package emt.emtlab.services.domain.service;

import emt.emtlab.services.domain.model.Country;
import java.util.List;
import java.util.Optional;

public interface CountryDomainService {
    List<Country> findAll();
    Optional<Country> findById(Long id);
    Country save(Country country);
    Optional<Country> update(Long id, Country countryDetails);
    void deleteById(Long id);
    List<Country> findByNameContaining(String name);
}