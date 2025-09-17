package nl.laura.boekenapi.service;

import nl.laura.boekenapi.exception.DuplicateLibraryItemException;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.model.LibraryItem;
import nl.laura.boekenapi.model.User;
import nl.laura.boekenapi.repository.BookRepository;
import nl.laura.boekenapi.repository.LibraryItemRepository;
import nl.laura.boekenapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LibraryItemService {

    private final LibraryItemRepository libraryRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public LibraryItemService(LibraryItemRepository libraryRepo,
                              BookRepository bookRepo,
                              UserRepository userRepo) {
        this.libraryRepo = libraryRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public List<LibraryItem> getLibraryForUser(Long userId) {
        return libraryRepo.findAllByUser_Id(userId);
    }

    @Transactional
    public LibraryItem addBook(Long userId, Long bookId) {
        // Voorkom duplicates
        libraryRepo.findByUser_IdAndBook_Id(userId, bookId)
                .ifPresent(li -> {
                    throw new DuplicateLibraryItemException(
                            "Boek " + bookId + " bestaat al in de library van gebruiker " + userId);
                });

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " niet gevonden"));

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book " + bookId + " niet gevonden"));

        LibraryItem item = new LibraryItem();
        item.setUser(user);
        item.setBook(book);
        item.setAddedAt(LocalDateTime.now());

        return libraryRepo.save(item);
    }

    @Transactional
    public void removeBook(Long userId, Long bookId) {
        LibraryItem item = libraryRepo.findByUser_IdAndBook_Id(userId, bookId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "LibraryItem niet gevonden voor userId=" + userId + " en bookId=" + bookId));

        libraryRepo.delete(item);
    }
}

