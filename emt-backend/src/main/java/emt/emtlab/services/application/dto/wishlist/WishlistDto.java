package emt.emtlab.services.application.dto.wishlist;

import emt.emtlab.services.application.dto.book.BookDto;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record WishlistDto(
    Long id,
    @NotNull Long userId,
    String username,
    List<BookDto> books
) {}