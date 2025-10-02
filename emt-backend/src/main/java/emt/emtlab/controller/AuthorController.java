package emt.emtlab.controller;

import emt.emtlab.services.application.dto.PageResponse;
import emt.emtlab.services.application.dto.author.AuthorDto;
import emt.emtlab.services.application.service.AuthorApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Author Operations", description = "API for managing authors")
public class AuthorController {

    private final AuthorApplicationService authorService;

    public AuthorController(AuthorApplicationService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @Operation(summary = "Get all authors", description = "Returns a list of all authors")
    public List<AuthorDto> getAllAuthors() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID", description = "Returns an author based on their ID")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) {
        return authorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new author", description = "Creates a new author")
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto authorDto) {
        AuthorDto createdAuthor = authorService.save(authorDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdAuthor.id())
                .toUri();
        return ResponseEntity.created(location).body(createdAuthor);
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update author", description = "Updates an existing author")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDto authorDto) {
        return authorService.update(id, authorDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete author", description = "Deletes an author")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pagination")
    @Operation(summary = "Get all authors", description = "Returns a paginated list of authors")
    public ResponseEntity<PageResponse<AuthorDto>> getAllAuthorsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<AuthorDto> authorPage = authorService.findAll(page, size, sortBy, direction);
        return ResponseEntity.ok(new PageResponse<>(authorPage));
    }
}
