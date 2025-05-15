package emt.emtlab.controller;

import emt.emtlab.services.application.dto.author.AuthorNameProjection;
import emt.emtlab.services.application.dto.author.AuthorsByCountryDto;
import emt.emtlab.services.application.dto.book.BooksByAuthorDto;
import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.repository.AuthorRepository;
import emt.emtlab.services.domain.repository.AuthorsByCountryViewRepository;
import emt.emtlab.services.domain.repository.BooksByAuthorViewRepository;
import emt.emtlab.services.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Views and Projections", description = "Views and Projections API")
public class ViewsAndProjectionsController {
    private final BooksByAuthorViewRepository booksByAuthorViewRepository;
    private final AuthorsByCountryViewRepository authorsByCountryViewRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;

    @Operation(summary = "Get books by author", description = "Get the number of books for each author with view")
    @GetMapping("/books/by-author")
    public List<BooksByAuthorDto> getBooksByAuthor() {
        return booksByAuthorViewRepository.findBookCountsByAuthor();
    }

    @Operation(summary = "Get authors by country", description = "Get the number of authors for each country with view")
    @GetMapping("/authors/by-country")
    public List<AuthorsByCountryDto> getAuthorsByCountry() {
        return authorsByCountryViewRepository.findAuthorCountsByCountry();
    }

    @Operation(summary = "Get author names", description = "Get all author names with projection")
    @GetMapping("/authors/names")
    public List<AuthorNameProjection> getAuthorNames() {
        return authorRepository.findAllProjectedBy();
    }
    @Operation(summary = "Get all users without roles", description = "Get all users with lazy loaded roles using entity graph")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAllWithoutRoles();
    }


}