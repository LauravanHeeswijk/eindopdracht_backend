package nl.laura.boekenapi.service;

import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.BookMapper;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @InjectMocks
    BookService bookService;

    @Test
    void getAllBooks_returnsDtos() {
        // Arrange 1
        Book b1 = new Book();
        Book b2 = new Book();
        when(bookRepository.findAll()).thenReturn(List.of(b1, b2));

        BookResponse r1 = new BookResponse();
        BookResponse r2 = new BookResponse();
        when(bookMapper.toResponse(b1)).thenReturn(r1);
        when(bookMapper.toResponse(b2)).thenReturn(r2);

        // Act 2
        List<BookResponse> result = bookService.getAllBooks();

        // Assert 3
        assertEquals(2, result.size());
        assertSame(r1, result.get(0));
        assertSame(r2, result.get(1));
    }

    @Test
    void getBookById_whenFound_mapsToDto() {
        // Arrange 1
        Long id = 1L;
        Book entity = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(entity));
        BookResponse dto = new BookResponse();
        when(bookMapper.toResponse(entity)).thenReturn(dto);

        // Act 2
        BookResponse result = bookService.getBookById(id);

        // Assert 3
        assertNotNull(result);
        assertSame(dto, result);
    }

    @Test
    void getBookById_whenNotFound_throwsResourceNotFound() {
        // Arrange 1
        Long missingId = 99L;
        when(bookRepository.findById(missingId)).thenReturn(Optional.empty());

        // Act + Assert 2+3
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(missingId));

        verifyNoInteractions(bookMapper);
    }
}
