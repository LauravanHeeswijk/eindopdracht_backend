package nl.laura.boekenapi.service;

import java.util.List;
import java.util.stream.Collectors;
import nl.laura.boekenapi.dto.BookRequest;
import nl.laura.boekenapi.dto.BookResponse;
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

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public BookService(final BookRepository bookRepository,
                       final AuthorRepository authorRepository,
                       final CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getAll() {
        return bookRepository.findAll().stream()
                .map(BookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookResponse getById(final Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boek niet gevonden: " + id));
        return BookMapper.toResponse(book);
    }

    public BookResponse create(final BookRequest request) {
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Auteur niet gevonden: " + request.getAuthorId()));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + request.getCategoryId()));

        if (bookRepository.countByTitleIgnoreCaseAndAuthor_Id(request.getTitle(), author.getId()) > 0) {
            throw new DuplicateResourceException("Boek met deze titel bij deze auteur bestaat al");
        }

        Book entity = BookMapper.toEntity(request, author, category);
        Book saved = bookRepository.save(entity);
        return BookMapper.toResponse(saved);
    }

    public BookResponse update(final Long id, final BookRequest request) {
        Book entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boek niet gevonden: " + id));

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Auteur niet gevonden: " + request.getAuthorId()));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + request.getCategoryId()));

        if (
                (
                        !entity.getTitle().equalsIgnoreCase(request.getTitle())
                                || !entity.getAuthor().getId().equals(author.getId())
                )
                        && bookRepository.countByTitleIgnoreCaseAndAuthor_Id(request.getTitle(), author.getId()) > 0
        ) {
            throw new DuplicateResourceException("Boek met deze titel bij deze auteur bestaat al");
        }

        BookMapper.updateEntity(entity, request, author, category);
        return BookMapper.toResponse(entity);
    }

    public void delete(final Long id) {
        Book entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boek niet gevonden: " + id));
        bookRepository.delete(entity);
    }
}

