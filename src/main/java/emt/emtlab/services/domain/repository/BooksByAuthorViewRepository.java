package emt.emtlab.services.domain.repository;


import emt.emtlab.services.application.dto.book.BooksByAuthorDto;
import emt.emtlab.services.domain.model.BooksByAuthorView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BooksByAuthorViewRepository extends JpaRepository<BooksByAuthorView, Long> {
    @Query(value = "SELECT * FROM book_counts_by_author", nativeQuery = true)
    List<BooksByAuthorDto> findBookCountsByAuthor();
}