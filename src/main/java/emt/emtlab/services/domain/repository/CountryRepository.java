package emt.emtlab.services.domain.repository;


import emt.emtlab.services.domain.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    List<Country> findByNameContainingIgnoreCase(String name);
}