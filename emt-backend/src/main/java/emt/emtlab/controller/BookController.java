package emt.emtlab.controller;

import emt.emtlab.services.application.dto.PageResponse;
import emt.emtlab.services.application.dto.book.BookDto;
import emt.emtlab.services.application.dto.book.BookRentalDto;
import emt.emtlab.services.application.service.BookApplicationService;
import emt.emtlab.services.domain.model.Category;
import emt.emtlab.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Operations", description = "API for managing books in the library")
public class BookController {

    private final BookApplicationService bookService;
    private final SecurityUtils securityUtils;

    public BookController(BookApplicationService bookService, SecurityUtils securityUtils) {
        this.bookService = bookService;
        this.securityUtils = securityUtils;
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

    @GetMapping("/recent")
    @Operation(summary = "Get recent books", description = "Returns the 10 most recently added books")
    public ResponseEntity<List<BookDto>> getRecentBooks() {
        List<BookDto> recentBooks = bookService.findRecentBooks();
        return ResponseEntity.ok(recentBooks);
    }

    //@PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
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

    //@PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Updates an existing book in the library")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
        return bookService.update(id, bookDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //@PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
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

    //@PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN','USER'})")
    @PostMapping("/{id}/rent")
    @Operation(summary = "Rent book", description = "Marks a book as rented by the current user")
    public ResponseEntity<BookDto> rentBook(@PathVariable Long id) {
        Long currentUserId = securityUtils.getCurrentUser().getId();
        return bookService.markAsRented(id, currentUserId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //@PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN','USER'})")
    @PostMapping("/{id}/return")
    @Operation(summary = "Return book", description = "Marks a book as returned by the current user")
    public ResponseEntity<BookDto> returnBook(@PathVariable Long id) {
        Long currentUserId = securityUtils.getCurrentUser().getId();
        return bookService.returnBook(id, currentUserId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //@PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @GetMapping("/rentals")
    @Operation(summary = "Get all rentals", description = "Returns a list of all active rentals")
    public ResponseEntity<List<BookRentalDto>> getAllRentals() {
        List<BookRentalDto> rentals = bookService.getAllActiveRentals();
        return ResponseEntity.ok(rentals);
    }

    //@PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN','USER'})")
    @GetMapping("/rentals/user")
    @Operation(summary = "Get current user rentals", description = "Returns a list of all rentals for the current user")
    public ResponseEntity<List<BookRentalDto>> getCurrentUserRentals() {
        Long currentUserId = securityUtils.getCurrentUser().getId();
        List<BookRentalDto> rentals = bookService.getUserRentals(currentUserId);
        return ResponseEntity.ok(rentals);
    }

    // Admin-only endpoint to view a specific user's rentals
    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rentals/user/{userId}")
    @Operation(summary = "Get user rentals (admin only)", description = "Returns a list of all rentals for a specific user")
    public ResponseEntity<List<BookRentalDto>> getUserRentals(@PathVariable Long userId) {
        List<BookRentalDto> rentals = bookService.getUserRentals(userId);
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/pagination")
    @Operation(summary = "Get all books", description = "Returns a paginated list of books")
    public ResponseEntity<PageResponse<BookDto>> getAllBooksWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<BookDto> bookPage = bookService.findAll(page, size, sortBy, direction);
        return ResponseEntity.ok(new PageResponse<>(bookPage));
    }
}