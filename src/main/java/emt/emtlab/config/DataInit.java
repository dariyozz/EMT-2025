package emt.emtlab.config;

import emt.emtlab.model.*;
import emt.emtlab.repository.AuthorRepository;
import emt.emtlab.repository.BookRepository;
import emt.emtlab.repository.CountryRepository;
import emt.emtlab.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

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

    @PostConstruct
    private void init() {
        // Create and save a country
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
        Book book1 = new Book("Book One", Category.NOVEL, author1, 10, false, false);
        Book book2 = new Book("Book Two", Category.THRILLER, author2, 5, false, false);

        User user = new User();
        user.setUsername("Dario");

        User user1 = new User();
        user1.setUsername("Dare");

        userRepository.save(user);
        userRepository.save(user1);
        bookRepository.save(book1);
        bookRepository.save(book2);
    }
}