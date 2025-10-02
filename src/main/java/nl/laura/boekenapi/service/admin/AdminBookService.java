package nl.laura.boekenapi.service.admin;

import nl.laura.boekenapi.dto.BookRequest;
import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.dto.BookUpdateRequest;
import nl.laura.boekenapi.exception.DuplicateResourceException;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.BookMapper;
import nl.laura.boekenapi.model.Author;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.model.Category;
import nl.laura.boekenapi.repository.AuthorRepository;
import nl.laura.boekenapi.repository.BookRepository;
import nl.laura.boekenapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminBookService {

    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;
    private final CategoryRepository categoryRepo;
    private final BookMapper mapper;

    public AdminBookService(BookRepository bookRepo, AuthorRepository authorRepo,
                            CategoryRepository categoryRepo, BookMapper mapper) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.categoryRepo = categoryRepo;
        this.mapper = mapper;
    }

    public List<BookResponse> getAll() {
        return bookRepo.findAll().stream().map(mapper::toResponse).toList();
    }

    public BookResponse getById(Long id) {
        Book b = bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: id=" + id));
        return mapper.toResponse(b);
    }

    public BookResponse create(BookRequest req) {
        Author a = authorRepo.findById(req.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: id=" + req.getAuthorId()));
        Category c = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: id=" + req.getCategoryId()));

        if (bookRepo.existsByTitleIgnoreCaseAndAuthor_Id(req.getTitle(), a.getId())) {
            throw new DuplicateResourceException("Book already exists: " + req.getTitle() + " by this author");
        }

        Book b = mapper.toEntity(req, a, c);
        return mapper.toResponse(bookRepo.save(b));
    }

    public BookResponse update(Long id, BookUpdateRequest req) {
        Book b = bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: id=" + id));

        Author a = null;
        if (req.getAuthorId() != null) {
            a = authorRepo.findById(req.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found: id=" + req.getAuthorId()));
        }

        Category c = null;
        if (req.getCategoryId() != null) {
            c = categoryRepo.findById(req.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: id=" + req.getCategoryId()));
        }

        mapper.updateEntity(req, b, a, c);

        return mapper.toResponse(bookRepo.save(b));
    }

    public void delete(Long id) {
        if (!bookRepo.existsById(id)) {
            throw new ResourceNotFoundException("Book not found: id=" + id);
        }
        bookRepo.deleteById(id);
    }
}
