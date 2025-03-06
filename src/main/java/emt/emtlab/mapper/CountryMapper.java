package emt.emtlab.mapper;

import emt.emtlab.model.dto.CountryDto;
import emt.emtlab.model.Country;
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
}