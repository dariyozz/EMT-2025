package emt.emtlab.services.domain.service;

import emt.emtlab.services.domain.model.Book;
import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.model.Wishlist;

import java.util.List;

public interface WishlistDomainService {
    // Original methods
    Wishlist getOrCreateWishlist(User user);
    Wishlist addBookToWishlist(User user, Book book);
    Wishlist removeBookFromWishlist(User user, Book book);
    List<Book> rentAllBooks(User user, Wishlist wishlist);
    void clearWishlist(Wishlist wishlist);

    // New methods that take IDs
    Wishlist getOrCreateWishlistByUserId(Long userId);
    Wishlist addBookToWishlistByIds(Long userId, Long bookId);
    Wishlist removeBookFromWishlistByIds(Long userId, Long bookId);
    List<Book> rentAllBooksFromWishlistByUserId(Long userId);
    void clearWishlistByUserId(Long userId);
}