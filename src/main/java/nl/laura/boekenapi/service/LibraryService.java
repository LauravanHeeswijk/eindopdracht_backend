package nl.laura.boekenapi.service;

import nl.laura.boekenapi.dto.LibraryItemResponse;
import nl.laura.boekenapi.exception.DuplicateResourceException;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.LibraryItemMapper;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.model.LibraryItem;
import nl.laura.boekenapi.model.User;
import nl.laura.boekenapi.repository.BookRepository;
import nl.laura.boekenapi.repository.LibraryItemRepository;
import nl.laura.boekenapi.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LibraryService {

    private final LibraryItemRepository libraryItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LibraryItemMapper libraryItemMapper;

    public LibraryService(LibraryItemRepository libraryItemRepository,
                          BookRepository bookRepository,
                          UserRepository userRepository,
                          LibraryItemMapper libraryItemMapper) {
        this.libraryItemRepository = libraryItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.libraryItemMapper = libraryItemMapper;
    }

    @Transactional
    public LibraryItemResponse addToLibrary(String userEmail, Long bookId) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: id=" + bookId));

        if (libraryItemRepository.existsByUserIdAndBookId(user.getId(), book.getId())) {
            throw new DuplicateResourceException("Book already in library");
        }

        LibraryItem li = new LibraryItem();
        li.setUser(user);
        li.setBook(book);
        li.setAddedAt(LocalDateTime.now());

        try {
            return libraryItemMapper.toResponse(libraryItemRepository.save(li));
        } catch (DataIntegrityViolationException e) {
            // fallback als de DB-unique constraint toch toeslaat
            throw new DuplicateResourceException("Book already in library");
        }
    }

    @Transactional
    public void removeFromLibrary(String userEmail, Long bookId) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        libraryItemRepository.findByUserIdAndBookId(user.getId(), bookId)
                .ifPresent(libraryItemRepository::delete);
        // idempotent: geen error als item niet bestaat → 204 in controller
    }

    @Transactional(readOnly = true)
    public List<LibraryItemResponse> listMyLibrary(String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        return libraryItemRepository.findAllByUserId(user.getId())
                .stream().map(libraryItemMapper::toResponse).toList();
    }
}
