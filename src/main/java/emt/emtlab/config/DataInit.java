package emt.emtlab.config;

import emt.emtlab.model.Author;
import emt.emtlab.model.Book;
import emt.emtlab.model.Category;
import emt.emtlab.model.Country;
import emt.emtlab.repository.AuthorRepository;
import emt.emtlab.repository.BookRepository;
import emt.emtlab.repository.CountryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInit {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CountryRepository countryRepository;

    public DataInit(BookRepository bookRepository, AuthorRepository authorRepository, CountryRepository countryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.countryRepository = countryRepository;
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
        Book book1 = new Book("Book One", Category.NOVEL, author1, 10, false);
        Book book2 = new Book("Book Two", Category.THRILLER, author2, 5, false);

        bookRepository.save(book1);
        bookRepository.save(book2);
    }
}