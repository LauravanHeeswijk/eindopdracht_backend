package nl.laura.boekenapi.service;

import nl.laura.boekenapi.exception.DuplicateLibraryItemException;
import nl.laura.boekenapi.exception.ResourceNotFoundException; // of jouw 404-exceptionnaam
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryItemServiceTest {

    @Mock LibraryItemRepository libraryRepo;
    @Mock UserRepository userRepo;
    @Mock BookRepository bookRepo;

    @InjectMocks LibraryItemService service;

    @Test
    void addBook_whenDuplicate_then409() {
        when(libraryRepo.findByUser_IdAndBook_Id(1L, 1L))
                .thenReturn(Optional.of(new LibraryItem()));

        assertThrows(DuplicateLibraryItemException.class, () -> service.addBook(1L, 1L));

        verify(libraryRepo, never()).save(any());
    }

    @Test
    void addBook_whenOk_saves() {

        when(libraryRepo.findByUser_IdAndBook_Id(1L, 1L))
                .thenReturn(Optional.empty());
        when(userRepo.findById(1L)).thenReturn(Optional.of(new User()));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(new Book()));
        when(libraryRepo.save(any(LibraryItem.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.addBook(1L, 1L);

        assertNotNull(result);
        verify(libraryRepo, times(1)).save(any(LibraryItem.class));
    }

    @Test
    void removeBook_whenNotExists_then404() {
        when(libraryRepo.findByUser_IdAndBook_Id(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.removeBook(1L, 1L));
        verify(libraryRepo, never()).delete(any(LibraryItem.class));
    }
}
