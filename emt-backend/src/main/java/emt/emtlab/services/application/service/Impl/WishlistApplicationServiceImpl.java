package emt.emtlab.services.application.service.Impl;

import emt.emtlab.mapper.BookMapper;
import emt.emtlab.mapper.WishlistMapper;
import emt.emtlab.services.application.dto.book.BookDto;
import emt.emtlab.services.application.dto.wishlist.WishlistDto;
import emt.emtlab.services.application.service.WishlistApplicationService;
import emt.emtlab.services.domain.model.Book;
import emt.emtlab.services.domain.model.Wishlist;
import emt.emtlab.services.domain.service.WishlistDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistApplicationServiceImpl implements WishlistApplicationService {

    private final WishlistDomainService wishlistDomainService;
    private final WishlistMapper wishlistMapper;
    private final BookMapper bookMapper;

    public WishlistApplicationServiceImpl(
            WishlistDomainService wishlistDomainService,
            WishlistMapper wishlistMapper,
            BookMapper bookMapper) {
        this.wishlistDomainService = wishlistDomainService;
        this.wishlistMapper = wishlistMapper;
        this.bookMapper = bookMapper;
    }

    @Override
    public WishlistDto getOrCreateWishlist(Long userId) {
        Wishlist wishlist = wishlistDomainService.getOrCreateWishlistByUserId(userId);
        return wishlistMapper.toDto(wishlist);
    }

    @Override
    public WishlistDto addBookToWishlist(Long userId, Long bookId) {
        Wishlist wishlist = wishlistDomainService.addBookToWishlistByIds(userId, bookId);
        return wishlistMapper.toDto(wishlist);
    }

    @Override
    public WishlistDto removeBookFromWishlist(Long userId, Long bookId) {
        Wishlist wishlist = wishlistDomainService.removeBookFromWishlistByIds(userId, bookId);
        return wishlistMapper.toDto(wishlist);
    }

    @Override
    public List<BookDto> rentAllBooksFromWishlist(Long userId) {
        List<Book> rentedBooks = wishlistDomainService.rentAllBooksFromWishlistByUserId(userId);
        return rentedBooks.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void clearWishlist(Long userId) {
        wishlistDomainService.clearWishlistByUserId(userId);
    }
}