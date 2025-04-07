package emt.emtlab.controller;

import emt.emtlab.utils.SecurityUtils;
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
    private final SecurityUtils securityUtils;

    @Autowired
    public WishlistController(WishlistApplicationService wishlistApplicationService, SecurityUtils securityUtils) {
        this.wishlistApplicationService = wishlistApplicationService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    @Operation(summary = "Get current user's wishlist",
            description = "Returns the wishlist for the current user or creates a new one if it doesn't exist")
    @PreAuthorize("hasAnyRole({'USER','LIBRARIAN','ADMIN'})")
    public ResponseEntity<WishlistDto> getWishlist() {
        Long currentUserId = securityUtils.getCurrentUser().getId();
        WishlistDto wishlist = wishlistApplicationService.getOrCreateWishlist(currentUserId);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/books/{bookId}")
    @Operation(summary = "Add book to wishlist",
            description = "Adds a book to the current user's wishlist if it has available copies")
    @PreAuthorize("hasAnyRole({'USER','LIBRARIAN','ADMIN'})")
    public ResponseEntity<WishlistDto> addBookToWishlist(@PathVariable Long bookId) {
        Long currentUserId = securityUtils.getCurrentUser().getId();
        WishlistDto wishlist = wishlistApplicationService.addBookToWishlist(currentUserId, bookId);
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/books/{bookId}")
    @Operation(summary = "Remove book from wishlist",
            description = "Removes a specific book from the current user's wishlist")
    @PreAuthorize("hasAnyRole({'USER','LIBRARIAN','ADMIN'})")
    public ResponseEntity<WishlistDto> removeBookFromWishlist(@PathVariable Long bookId) {
        Long currentUserId = securityUtils.getCurrentUser().getId();
        WishlistDto wishlist = wishlistApplicationService.removeBookFromWishlist(currentUserId, bookId);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/rent-all")
    @Operation(summary = "Rent all books from wishlist",
            description = "Rents all books from the current user's wishlist and clears the wishlist")
    @PreAuthorize("hasAnyRole({'USER','LIBRARIAN','ADMIN'})")
    public ResponseEntity<List<BookDto>> rentAllFromWishlist() {
        Long currentUserId = securityUtils.getCurrentUser().getId();
        List<BookDto> rentedBooks = wishlistApplicationService.rentAllBooksFromWishlist(currentUserId);
        return ResponseEntity.ok(rentedBooks);
    }

    @DeleteMapping
    @Operation(summary = "Clear wishlist",
            description = "Removes all books from the current user's wishlist")
    @PreAuthorize("hasAnyRole({'USER','LIBRARIAN','ADMIN'})")
    public ResponseEntity<Void> clearWishlist() {
        Long currentUserId = securityUtils.getCurrentUser().getId();
        wishlistApplicationService.clearWishlist(currentUserId);
        return ResponseEntity.noContent().build();
    }

    // Admin-only endpoint to get a specific user's wishlist
    @GetMapping("/admin/user/{userId}")
    @Operation(summary = "Get specific user's wishlist (admin only)",
            description = "Returns the wishlist for a specific user (admin access only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WishlistDto> getUserWishlist(@PathVariable Long userId) {
        WishlistDto wishlist = wishlistApplicationService.getOrCreateWishlist(userId);
        return ResponseEntity.ok(wishlist);
    }
}