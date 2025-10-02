package emt.emtlab.controller;

import emt.emtlab.services.application.dto.PageResponse;
import emt.emtlab.services.application.dto.country.CountryDto;
import emt.emtlab.services.application.service.CountryApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/countries")
@Tag(name = "Country Operations", description = "API for managing countries")
public class CountryController {

    private final CountryApplicationService countryService;

    public CountryController(CountryApplicationService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @Operation(summary = "Get all countries", description = "Returns a list of all countries")
    public List<CountryDto> getAllCountries() {
        return countryService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get country by ID", description = "Returns a country based on its ID")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable Long id) {
        return countryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //@PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @PostMapping
    @Operation(summary = "Create a new country", description = "Creates a new country")
    public ResponseEntity<CountryDto> createCountry(@Valid @RequestBody CountryDto countryDto) {
        CountryDto createdCountry = countryService.save(countryDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCountry.id())
                .toUri();
        return ResponseEntity.created(location).body(createdCountry);
    }

    //@PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @PutMapping("/{id}")
    @Operation(summary = "Update country", description = "Updates an existing country")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable Long id, @Valid @RequestBody CountryDto countryDto) {
        return countryService.update(id, countryDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //@PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete country", description = "Deletes a country")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        countryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pagination")
    @Operation(summary = "Get all countries", description = "Returns a paginated list of countries")
    public ResponseEntity<PageResponse<CountryDto>> getAllCountriesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<CountryDto> countryPage = countryService.findAll(page, size, sortBy, direction);
        return ResponseEntity.ok(new PageResponse<>(countryPage));
    }

}