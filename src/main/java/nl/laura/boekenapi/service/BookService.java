package nl.laura.boekenapi.service;

import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.BookMapper;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional (readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    // User story 1: alle boeken kunnen zien

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }
    // User story 2: details van 1 boek kunnen zien

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Boek met id " + id + " niet gevonden"));
        return bookMapper.toResponse(book);
    }
}
