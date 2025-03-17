package emt.emtlab.repository;

import emt.emtlab.model.Book;
import emt.emtlab.model.BookRental;
import emt.emtlab.model.User;
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