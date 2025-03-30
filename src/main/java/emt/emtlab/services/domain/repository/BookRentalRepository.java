package emt.emtlab.services.domain.repository;

import emt.emtlab.services.domain.model.Book;
import emt.emtlab.services.domain.model.BookRental;
import emt.emtlab.services.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRentalRepository extends JpaRepository<BookRental, Long> {
    List<BookRental> findByUserAndReturnDateIsNull(User user);
    List<BookRental> findByReturnDateIsNull();
    Optional<BookRental> findByBookAndReturnDateIsNull(Book book);
}