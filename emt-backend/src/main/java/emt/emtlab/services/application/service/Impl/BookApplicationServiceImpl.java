package emt.emtlab.services.application.service.Impl;

import emt.emtlab.exceptions.ResourceNotFoundException;
import emt.emtlab.mapper.BookMapper;
import emt.emtlab.services.application.dto.book.BookDto;
import emt.emtlab.services.application.dto.book.BookRentalDto;
import emt.emtlab.services.application.service.BookApplicationService;
import emt.emtlab.services.domain.model.Author;
import emt.emtlab.services.domain.model.Book;
import emt.emtlab.services.domain.model.Category;
import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.repository.BookRepository;
import emt.emtlab.services.domain.service.AuthorDomainService;
import emt.emtlab.services.domain.service.BookDomainService;
import emt.emtlab.services.domain.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookApplicationServiceImpl implements BookApplicationService {

    private final BookDomainService bookDomainService;
    private final AuthorDomainService authorDomainService;
    private final UserDomainService userDomainService;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookApplicationServiceImpl(BookDomainService bookDomainService,
                                      AuthorDomainService authorDomainService,
                                      UserDomainService userDomainService, BookRepository bookRepository, BookMapper bookMapper) {
        this.bookDomainService = bookDomainService;
        this.authorDomainService = authorDomainService;
        this.userDomainService = userDomainService;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookDto> findAll() {
        return bookDomainService.findAll().stream()
                .map(BookDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDto> findById(Long id) {
        return bookDomainService.findById(id).map(BookDto::from);
    }

    @Override
    @Transactional
    public BookDto save(BookDto bookDto) {
        var author = authorDomainService.findById(bookDto.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + bookDto.authorId()));

        Book book = bookDto.toBook(new Book(), author);
        return BookDto.from(bookDomainService.save(book));
    }

    @Override
    @Transactional
    public Optional<BookDto> update(Long id, BookDto bookDto) {
        Author author = authorDomainService.findById(bookDto.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + bookDto.authorId()));

        Book updatedBook = bookDomainService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        updatedBook.setAuthor(author);

        return bookDomainService.update(updatedBook, bookDto).map(BookDto::from);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bookDomainService.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<BookDto> markAsRented(Long bookId, Long userId) {
        User user = userDomainService.findById(userId);

        Book book = bookDomainService.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        return bookDomainService.markAsRented(book, user).map(BookDto::from);
    }

    @Override
    @Transactional
    public Optional<BookDto> returnBook(Long bookId, Long userId) {
        User user = userDomainService.findById(userId);

        Book book = bookDomainService.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        return bookDomainService.returnBook(book, user).map(BookDto::from);
    }

    @Override
    public List<BookRentalDto> getAllActiveRentals() {
        return bookDomainService.getAllActiveRentals().stream()
                .map(BookRentalDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookRentalDto> getUserRentals(Long userId) {
        User user = userDomainService.findById(userId);

        return bookDomainService.getUserRentals(user).stream()
                .map(BookRentalDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> getAllCategories() {
        return Arrays.asList(Category.values());
    }

    @Override
    public List<BookDto> filterBooks(String name, Long authorId, String category) {
        Category categoryEnum = null;
        if (category != null && !category.isEmpty()) {
            try {
                categoryEnum = Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid category provided, will be treated as null
            }
        }

        return bookDomainService.filterBooks(name, authorId, categoryEnum).stream()
                .map(BookDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> findRecentBooks() {
        return bookDomainService.findRecentBooks().stream()
                .map(BookDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookDto> findAll(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toLowerCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return bookRepository.findAll(pageable).map(bookMapper::toDto);
    }
}
