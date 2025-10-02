package emt.emtlab.services.domain.repository;

import emt.emtlab.services.domain.model.Book;
import emt.emtlab.services.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE " +
            "(:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:authorId IS NULL OR b.author.id = :authorId) AND " +
            "(:category IS NULL OR b.category = :category) AND " +
            "b.deleted = false")
    List<Book> findBooksByFilters(@Param("name") String name,
                                  @Param("authorId") Long authorId,
                                  @Param("category") Category category);


//    @Query(value = "SELECT * FROM book b WHERE b.deleted = false ORDER BY b.createdAt DESC LIMIT :limit",
//            nativeQuery = true)
//    List<Book> findRecentBooks(@Param("limit") int limit);
    List<Book> findTop10ByDeletedFalseOrderByCreatedAtDesc();
}