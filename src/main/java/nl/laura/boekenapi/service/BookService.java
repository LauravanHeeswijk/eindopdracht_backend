package nl.laura.boekenapi.service;

import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.dto.BookRequest; // ← gebruik jouw eigen naam hier
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.BookMapper;
import nl.laura.boekenapi.model.Author;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.model.Category;
import nl.laura.boekenapi.repository.AuthorRepository;
import nl.laura.boekenapi.repository.BookRepository;
import nl.laura.boekenapi.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       CategoryRepository categoryRepository,
                       BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boek met id " + id + " niet gevonden"));
        return bookMapper.toResponse(book);
    }

    private void orElseThrow(Object o) {
    }

    @Transactional
    public BookResponse create(BookRequest dto) {
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auteur niet gevonden"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cetagorie niet gevonden"));

        Book b = new Book();
        b.setTitle(dto.getTitle());
        b.setDescription(dto.getDescription());
        b.setPublicationYear(dto.getPublicationYear());
        b.setAuthor(author);
        b.setCategory(category);

        Book saved = bookRepository.save(b);
        return bookMapper.toResponse(saved);
    }
}
