package emt.emtlab.controller;

import emt.emtlab.services.application.dto.book.BookDto;
import emt.emtlab.services.application.dto.wishlist.WishlistDto;
import emt.emtlab.services.application.service.WishlistApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "Wishlist Operations", description = "API for managing user wishlists")
public class WishlistController {

    private final WishlistApplicationService wishlistApplicationService;

    @Autowired
    public WishlistController(WishlistApplicationService wishlistApplicationService) {
        this.wishlistApplicationService = wishlistApplicationService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user's wishlist",
               description = "Returns the wishlist for a specific user or creates a new one if it doesn't exist")
    @PreAuthorize("hasAnyRole({'USER','LIBRARIAN','ADMIN'})")
    public ResponseEntity<WishlistDto> getWishlist(@PathVariable Long userId) {
        WishlistDto wishlist = wishlistApplicationService.getOrCreateWishlist(userId);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/{userId}/books/{bookId}")
    @Operation(summary = "Add book to wishlist",
               description = "Adds a book to the user's wishlist if it has available copies")
    @PreAuthorize("hasAnyRole({'USER','LIBRARIAN','ADMIN'})")
    public ResponseEntity<WishlistDto> addBookToWishlist(@PathVariable Long userId, @PathVariable Long bookId) {
        WishlistDto wishlist = wishlistApplicationService.addBookToWishlist(userId, bookId);
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/{userId}/books/{bookId}")
    @Operation(summary = "Remove book from wishlist",
               description = "Removes a specific book from the user's wishlist")
    @PreAuthorize("hasAnyRole({'USER','LIBRARIAN','ADMIN'})")
    public ResponseEntity<WishlistDto> removeBookFromWishlist(@PathVariable Long userId, @PathVariable Long bookId) {
        WishlistDto wishlist = wishlistApplicationService.removeBookFromWishlist(userId, bookId);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/{userId}/rent-all")
    @Operation(summary = "Rent all books from wishlist",
               description = "Rents all books from the user's wishlist and clears the wishlist")
    @PreAuthorize("hasAnyRole({'USER','LIBRARIAN','ADMIN'})")
    public ResponseEntity<List<BookDto>> rentAllFromWishlist(@PathVariable Long userId) {
        List<BookDto> rentedBooks = wishlistApplicationService.rentAllBooksFromWishlist(userId);
        return ResponseEntity.ok(rentedBooks);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Clear wishlist",
               description = "Removes all books from the user's wishlist")
    @PreAuthorize("hasAnyRole({'USER','LIBRARIAN','ADMIN'})")
    public ResponseEntity<Void> clearWishlist(@PathVariable Long userId) {
        wishlistApplicationService.clearWishlist(userId);
        return ResponseEntity.noContent().build();
    }
}