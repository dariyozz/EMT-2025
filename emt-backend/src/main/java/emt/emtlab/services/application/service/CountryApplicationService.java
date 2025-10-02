package emt.emtlab.services.application.service;

import emt.emtlab.services.application.dto.country.CountryDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CountryApplicationService {
    List<CountryDto> findAll();

    Optional<CountryDto> findById(Long id);

    CountryDto save(CountryDto countryDto);

    Optional<CountryDto> update(Long id, CountryDto countryDto);

    void deleteById(Long id);

    List<CountryDto> findByNameContaining(String name);

    Page<CountryDto> findAll(int page, int size, String sortBy, String direction);
}