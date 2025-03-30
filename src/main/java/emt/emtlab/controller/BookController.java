package emt.emtlab.controller;

import emt.emtlab.services.domain.model.Category;
import emt.emtlab.services.application.dto.book.BookDto;
import emt.emtlab.services.application.dto.book.BookRentalDto;
import emt.emtlab.services.application.service.BookApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Operations", description = "API for managing books in the library")
//@CrossOrigin(origins = "*")
public class BookController {

    private final BookApplicationService bookService;

    public BookController(BookApplicationService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Returns a list of all books in the library")
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Returns a book based on its ID")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/filter-book")
    @Operation(summary = "Filter books", description = "Returns a list of books filtered by name, author and category")
    public ResponseEntity<List<BookDto>> filterBookByNameAuthorAndCategory(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String category
    ) {
        List<BookDto> filteredBooks = bookService.filterBooks(name, authorId, category);
        return ResponseEntity.ok(filteredBooks);
    }

    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @PostMapping
    @Operation(summary = "Create a new book", description = "Creates a new book in the library")
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) {
        BookDto createdBook = bookService.save(bookDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdBook.id())
                .toUri();
        return ResponseEntity.created(location).body(createdBook);
    }


    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Updates an existing book in the library")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
        return bookService.update(id, bookDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book", description = "Deletes a book from the library")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Returns a list of all book categories")
    public List<Category> getAllCategories() {
        return bookService.getAllCategories();
    }

    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @PostMapping("/{id}/rent/{userId}")
    @Operation(summary = "Rent book", description = "Marks a book as rented by a specific user")
    public ResponseEntity<BookDto> rentBook(@PathVariable Long id, @PathVariable Long userId) {
        return bookService.markAsRented(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @PostMapping("/{id}/return/{userId}")
    @Operation(summary = "Return book", description = "Marks a book as returned by a specific user")
    public ResponseEntity<BookDto> returnBook(@PathVariable Long id, @PathVariable Long userId) {
        return bookService.returnBook(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @GetMapping("/rentals")
    @Operation(summary = "Get all rentals", description = "Returns a list of all active rentals")
    public ResponseEntity<List<BookRentalDto>> getAllRentals() {
        List<BookRentalDto> rentals = bookService.getAllActiveRentals();
        return ResponseEntity.ok(rentals);
    }

    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN', 'USER'})")
    @GetMapping("/rentals/user/{userId}")
    @Operation(summary = "Get user rentals", description = "Returns a list of all rentals for a specific user")
    public ResponseEntity<List<BookRentalDto>> getUserRentals(@PathVariable Long userId) {
        List<BookRentalDto> rentals = bookService.getUserRentals(userId);
        return ResponseEntity.ok(rentals);
    }
}