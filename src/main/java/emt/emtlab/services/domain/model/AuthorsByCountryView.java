package emt.emtlab.services.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "author_counts_by_country")
public class AuthorsByCountryView {
    @Id
    @Column(name = "country_id")
    private Long countryId;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "continent")
    private String continent;

    @Column(name = "author_count")
    private Long authorCount;
}