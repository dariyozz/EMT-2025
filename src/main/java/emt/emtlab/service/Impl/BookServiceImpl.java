package emt.emtlab.service.Impl;

import emt.emtlab.exceptions.ResourceNotFoundException;
import emt.emtlab.mapper.BookMapper;
import emt.emtlab.model.Author;
import emt.emtlab.model.Book;
import emt.emtlab.model.Category;
import emt.emtlab.model.dto.BookDto;
import emt.emtlab.repository.AuthorRepository;
import emt.emtlab.repository.BookRepository;
import emt.emtlab.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDto> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto);
    }

    @Override
    @Transactional
    public BookDto save(BookDto bookDto) {
        Author author = authorRepository.findById(bookDto.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + bookDto.authorId()));

        Book book = new Book();
        book.setName(bookDto.name());
        book.setCategory(bookDto.category());
        book.setAuthor(author);
        book.setAvailableCopies(bookDto.availableCopies());
        book.setRented(bookDto.rented());

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public Optional<BookDto> update(Long id, BookDto bookDto) {
        return bookRepository.findById(id)
                .map(book -> {
                    Author author = authorRepository.findById(bookDto.authorId())
                            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + bookDto.authorId()));

                    book.setName(bookDto.name());
                    book.setCategory(bookDto.category());
                    book.setAuthor(author);
                    book.setAvailableCopies(bookDto.availableCopies());
                    book.setRented(bookDto.rented());

                    return bookMapper.toDto(bookRepository.save(book));
                });
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<BookDto> markAsRented(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    if (book.getAvailableCopies() > 0) {
                        book.setAvailableCopies(book.getAvailableCopies() - 1);
                        if (book.getAvailableCopies() == 0) {
                            book.setRented(true);
                        }
                        return bookMapper.toDto(bookRepository.save(book));
                    }
                    throw new IllegalStateException("No available copies to rent");
                });
    }

    @Override
    public List<Category> getAllCategories() {
        return Arrays.asList(Category.values());
    }
}