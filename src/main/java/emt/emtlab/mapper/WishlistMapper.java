package emt.emtlab.mapper;

import emt.emtlab.services.domain.model.Wishlist;
import emt.emtlab.services.application.dto.wishlist.WishlistDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WishlistMapper {
    private final BookMapper bookMapper;

    @Autowired
    public WishlistMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public WishlistDto toDto(Wishlist wishlist) {
        return new WishlistDto(
            wishlist.getId(),
            wishlist.getUser().getId(),
            wishlist.getUser().getUsername(),
            wishlist.getBooks().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList())
        );
    }
}