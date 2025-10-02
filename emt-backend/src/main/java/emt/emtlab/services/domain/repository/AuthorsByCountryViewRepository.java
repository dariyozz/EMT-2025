package emt.emtlab.services.domain.repository;


import emt.emtlab.services.application.dto.author.AuthorsByCountryDto;
import emt.emtlab.services.domain.model.AuthorsByCountryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorsByCountryViewRepository extends JpaRepository<AuthorsByCountryView, Long> {
    @Query(value = "SELECT * FROM author_counts_by_country", nativeQuery = true)
    List<AuthorsByCountryDto> findAuthorCountsByCountry();
}