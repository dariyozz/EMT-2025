package emt.emtlab.services.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Data
@Entity
@Subselect("Select * from book_counts_by_author")
@Table(name = "book_counts_by_author")
@Immutable
public class BooksByAuthorView {
    @Id
    @Column(name = "author_id")
    private Long authorId;
    @Column(name = "author_name")
    private String authorName;
    @Column(name = "author_surname")
    private String authorSurname;
    @Column(name = "book_count")
    private Long bookCount;
}