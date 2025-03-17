package emt.emtlab.controller;

import emt.emtlab.model.Category;
import emt.emtlab.model.dto.BookDto;
import emt.emtlab.model.dto.BookRentalDto;
import emt.emtlab.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
//@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/filter-book")
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
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
        return bookService.update(id, bookDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return bookService.getAllCategories();
    }

    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @PostMapping("/{id}/rent/{userId}")
    public ResponseEntity<BookDto> rentBook(@PathVariable Long id, @PathVariable Long userId) {
        return bookService.markAsRented(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @PostMapping("/{id}/return/{userId}")
    public ResponseEntity<BookDto> returnBook(@PathVariable Long id, @PathVariable Long userId) {
        return bookService.returnBook(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN'})")
    @GetMapping("/rentals")
    public ResponseEntity<List<BookRentalDto>> getAllRentals() {
        List<BookRentalDto> rentals = bookService.getAllActiveRentals();
        return ResponseEntity.ok(rentals);
    }

    @PreAuthorize("hasAnyRole({'LIBRARIAN','ADMIN', 'USER'})")
    @GetMapping("/rentals/user/{userId}")
    public ResponseEntity<List<BookRentalDto>> getUserRentals(@PathVariable Long userId) {
        List<BookRentalDto> rentals = bookService.getUserRentals(userId);
        return ResponseEntity.ok(rentals);
    }
}