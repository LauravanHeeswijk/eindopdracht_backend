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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock private LibraryItemRepository libraryItemRepository;
    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;

    @Spy  private LibraryItemMapper libraryItemMapper = new LibraryItemMapper();

    @InjectMocks private LibraryService service;

    @Test
    void addToLibrary_ok() {
        var user = new User(); user.setId(1L); user.setEmail("e@x.com");
        var book = new Book(); book.setId(2L); book.setTitle("Stoic");

        when(userRepository.findByEmail("e@x.com")).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(libraryItemRepository.existsByUserIdAndBookId(1L, 2L)).thenReturn(false);

        var saved = new LibraryItem();
        saved.setId(10L); saved.setUser(user); saved.setBook(book); saved.setAddedAt(LocalDateTime.now());
        when(libraryItemRepository.save(any(LibraryItem.class))).thenReturn(saved);

        LibraryItemResponse resp = service.addToLibrary("e@x.com", 2L);

        assertEquals(10L, resp.getId());
        assertEquals(1L, resp.getUserId());
        assertEquals(2L, resp.getBookId());
        assertEquals("Stoic", resp.getBookTitle());
    }

    @Test
    void addToLibrary_duplicate_throws409() {
        var user = new User(); user.setId(1L); user.setEmail("e@x.com");
        var book = new Book(); book.setId(2L);

        when(userRepository.findByEmail("e@x.com")).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(libraryItemRepository.existsByUserIdAndBookId(1L, 2L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.addToLibrary("e@x.com", 2L));
    }

    @Test
    void addToLibrary_bookNotFound_throws404() {
        var user = new User(); user.setId(1L); user.setEmail("e@x.com");

        when(userRepository.findByEmail("e@x.com")).thenReturn(Optional.of(user));
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.addToLibrary("e@x.com", 999L));
    }

    @Test
    void listMyLibrary_ok() {
        var user = new User(); user.setId(1L); user.setEmail("e@x.com");
        var book = new Book(); book.setId(2L); book.setTitle("Stoic");
        var li = new LibraryItem(); li.setId(5L); li.setUser(user); li.setBook(book); li.setAddedAt(LocalDateTime.now());

        when(userRepository.findByEmail("e@x.com")).thenReturn(Optional.of(user));
        when(libraryItemRepository.findAllByUserId(1L)).thenReturn(List.of(li));

        var list = service.listMyLibrary("e@x.com");
        assertEquals(1, list.size());
        assertEquals(5L, list.get(0).getId());
        assertEquals("Stoic", list.get(0).getBookTitle());
    }
}
