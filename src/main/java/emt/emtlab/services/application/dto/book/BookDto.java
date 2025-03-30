package emt.emtlab.services.application.dto.book;

import emt.emtlab.services.domain.model.Author;
import emt.emtlab.services.domain.model.Book;
import emt.emtlab.services.domain.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record BookDto(
        Long id,
        @NotBlank(message = "Book name cannot be blank")
        String name,
        @NotNull(message = "Category is required")
        Category category,
        @NotNull(message = "Author is required")
        Long authorId,
        @Min(value = 0, message = "Available copies must be non-negative")
        int availableCopies,
        boolean rented,
        boolean deleted
) {
        public static BookDto from(Book book){
                return new BookDto(
                        book.getId(),
                        book.getName(),
                        book.getCategory(),
                        book.getAuthor().getId(),
                        book.getAvailableCopies(),
                        book.isRented(),
                        book.isDeleted()
                );
        }


        public Book toBook(Book book, Author author) {
                book.setName(name);
                book.setCategory(category);
                book.setAuthor(author);
                book.setAvailableCopies(availableCopies);
                book.setRented(rented);
                book.setDeleted(deleted);
                return book;
        }
}