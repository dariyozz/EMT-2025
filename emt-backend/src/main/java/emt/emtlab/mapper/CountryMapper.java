package emt.emtlab.mapper;

import emt.emtlab.services.application.dto.country.CountryDto;
import emt.emtlab.services.domain.model.Country;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {

    public CountryDto toDto(Country country) {
        return new CountryDto(
                country.getId(),
                country.getName(),
                country.getContinent()
        );
    }

    public Country toEntity(CountryDto countryDto) {
        return new Country(
                countryDto.name(),
                countryDto.continent()
        );
    }
}