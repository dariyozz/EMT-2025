package emt.emtlab.config;

import emt.emtlab.services.domain.model.Author;
import emt.emtlab.services.domain.model.Book;
import emt.emtlab.services.domain.model.Category;
import emt.emtlab.services.domain.model.Country;
import emt.emtlab.services.domain.repository.AuthorRepository;
import emt.emtlab.services.domain.repository.BookRepository;
import emt.emtlab.services.domain.repository.CountryRepository;
import emt.emtlab.services.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInit {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;

    public DataInit(BookRepository bookRepository, AuthorRepository authorRepository, CountryRepository countryRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
    }

   // @PostConstruct
    private void init() {
        // Create and save a country
        if (userRepository.findAll().isEmpty()) {
            Country country = new Country();
            country.setName("USA");
            country.setContinent("North America");
            countryRepository.save(country);

            // Create authors with country relation
            Author author1 = new Author();
            author1.setName("Author");
            author1.setSurname("One");
            author1.setCountry(country);
            authorRepository.save(author1);

            Author author2 = new Author();
            author2.setName("Author");
            author2.setSurname("Two");
            author2.setCountry(country);
            authorRepository.save(author2);

            // Create books with associated authors and enum category
            // Create some books with different dates (some older, some newer)
            Book book1 = new Book("Book One", Category.NOVEL, author1, 10, false, false, LocalDateTime.now().minusMonths(6));
            Book book2 = new Book("Book Two", Category.THRILLER, author2, 5, false, false, LocalDateTime.now().minusMonths(4));
            Book book3 = new Book("Book Three", Category.FANTASY, author1, 7, false, false, LocalDateTime.now().minusMonths(3));
            Book book4 = new Book("Book Four", Category.BIOGRAPHY, author2, 3, false, false, LocalDateTime.now().minusMonths(2));
            Book book5 = new Book("Book Five", Category.HISTORY, author1, 8, false, false, LocalDateTime.now().minusMonths(1));
            Book book6 = new Book("Book Six", Category.NOVEL, author2, 6, false, false, LocalDateTime.now().minusDays(20));
            Book book7 = new Book("Book Seven", Category.THRILLER, author1, 4, false, false, LocalDateTime.now().minusDays(15));
            Book book8 = new Book("Book Eight", Category.FANTASY, author2, 9, false, false, LocalDateTime.now().minusDays(10));
            Book book9 = new Book("Book Nine", Category.BIOGRAPHY, author1, 2, false, false, LocalDateTime.now().minusDays(5));
            Book book10 = new Book("Book Ten", Category.HISTORY, author2, 7, false, false, LocalDateTime.now());
            Book book11 = new Book("Book Eleven", Category.NOVEL, author1, 5, false, false, LocalDateTime.now().plusDays(5));

            bookRepository.saveAll(List.of(book1, book2, book3, book4, book5, book6, book7, book8, book9, book10, book11));
        }
    }
}