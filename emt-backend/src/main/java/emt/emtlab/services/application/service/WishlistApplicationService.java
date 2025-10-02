package emt.emtlab.services.application.service;

import emt.emtlab.services.application.dto.book.BookDto;
import emt.emtlab.services.application.dto.wishlist.WishlistDto;

import java.util.List;

public interface WishlistApplicationService {
    WishlistDto getOrCreateWishlist(Long userId);
    WishlistDto addBookToWishlist(Long userId, Long bookId);
    WishlistDto removeBookFromWishlist(Long userId, Long bookId);
    List<BookDto> rentAllBooksFromWishlist(Long userId);
    void clearWishlist(Long userId);
}