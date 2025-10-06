package nl.laura.boekenapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LibraryService {

    private final LibraryItemRepository libraryItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LibraryService(final LibraryItemRepository libraryItemRepository,
                          final BookRepository bookRepository,
                          final UserRepository userRepository) {
        this.libraryItemRepository = libraryItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public LibraryItemResponse addToLibrary(final String userEmail, final Long bookId) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Gebruiker niet gevonden: " + userEmail));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Boek niet gevonden: " + bookId));

        if (libraryItemRepository.countByUserIdAndBookId(user.getId(), book.getId()) > 0) {
            throw new DuplicateResourceException("Dit boek staat al in je bibliotheek");
        }

        LibraryItem li = new LibraryItem();
        li.setUser(user);
        li.setBook(book);
        li.setAddedAt(LocalDateTime.now());

        LibraryItem saved = libraryItemRepository.save(li);
        return LibraryItemMapper.toResponse(saved);
    }

    public void removeFromLibrary(final String userEmail, final Long bookId) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Gebruiker niet gevonden: " + userEmail));

        libraryItemRepository.findByUserIdAndBookId(user.getId(), bookId)
                .ifPresent(libraryItemRepository::delete);
        // idempotent: als er niks is, doen we niks → controller geeft 204
    }

    @Transactional(readOnly = true)
    public List<LibraryItemResponse> listMyLibrary(final String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Gebruiker niet gevonden: " + userEmail));

        return libraryItemRepository.findAllByUserId(user.getId()).stream()
                .map(LibraryItemMapper::toResponse)
                .collect(Collectors.toList());
    }
}
