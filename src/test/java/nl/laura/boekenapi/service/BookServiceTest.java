package nl.laura.boekenapi.service;

import nl.laura.boekenapi.dto.BookRequest;
import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.exception.DuplicateResourceException;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.model.Author;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.model.Category;
import nl.laura.boekenapi.model.FileAsset;
import nl.laura.boekenapi.repository.AuthorRepository;
import nl.laura.boekenapi.repository.BookRepository;
import nl.laura.boekenapi.repository.CategoryRepository;
import nl.laura.boekenapi.repository.FileAssetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock BookRepository bookRepository;
    @Mock AuthorRepository authorRepository;
    @Mock CategoryRepository categoryRepository;
    @Mock FileAssetRepository fileRepo; //

    @InjectMocks BookService bookService;

    @Test
    void createOk() {
        BookRequest req = new BookRequest();
        req.setTitle("Atomic Habits");
        req.setDescription("Korte beschrijving");
        req.setPublicationYear(2018);
        req.setAuthorId(1L);
        req.setCategoryId(2L);

        Author author = new Author();
        author.setId(1L);
        author.setName("Ryan Holiday");

        Category category = new Category();
        category.setId(2L);
        category.setName("Productivity");

        Book saved = new Book();
        saved.setId(10L);
        saved.setTitle(req.getTitle());
        saved.setDescription(req.getDescription());
        saved.setPublicationYear(req.getPublicationYear());
        saved.setAuthor(author);
        saved.setCategory(category);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(bookRepository.countByTitleIgnoreCaseAndAuthor_Id("Atomic Habits", 1L)).thenReturn(0L);
        when(bookRepository.save(org.mockito.ArgumentMatchers.any(Book.class))).thenReturn(saved);

        BookResponse result = bookService.create(req);

        assertEquals(10L, result.getId());
        assertEquals("Atomic Habits", result.getTitle());
        assertEquals(2018, result.getPublicationYear());
        assertEquals("Ryan Holiday", result.getAuthor().getName());
        assertEquals("Productivity", result.getCategory().getName());
    }

    @Test
    void createAuthorNotFound() {
        BookRequest req = new BookRequest();
        req.setTitle("T");
        req.setAuthorId(999L);
        req.setCategoryId(2L);

        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.create(req));
    }

    @Test
    void createCategoryNotFound() {
        BookRequest req = new BookRequest();
        req.setTitle("T");
        req.setAuthorId(1L);
        req.setCategoryId(999L);

        Author author = new Author();
        author.setId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.create(req));
    }

    @Test
    void createDuplicateForSameAuthor() {
        BookRequest req = new BookRequest();
        req.setTitle("Existing");
        req.setAuthorId(1L);
        req.setCategoryId(2L);

        Author author = new Author();
        author.setId(1L);
        Category category = new Category();
        category.setId(2L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(bookRepository.countByTitleIgnoreCaseAndAuthor_Id("Existing", 1L)).thenReturn(1L);

        assertThrows(DuplicateResourceException.class, () -> bookService.create(req));
    }

    @Test
    void getAllOk() {
        Author a = new Author(); a.setId(1L); a.setName("Author A");
        Category c = new Category(); c.setId(2L); c.setName("Cat");

        Book b1 = new Book(); b1.setId(1L); b1.setTitle("T1"); b1.setPublicationYear(2000); b1.setAuthor(a); b1.setCategory(c);
        Book b2 = new Book(); b2.setId(2L); b2.setTitle("T2"); b2.setPublicationYear(2001); b2.setAuthor(a); b2.setCategory(c);

        when(bookRepository.findAll()).thenReturn(Arrays.asList(b1, b2));

        List<BookResponse> list = bookService.getAll();

        assertEquals(2, list.size());
        assertEquals("T1", list.get(0).getTitle());
        assertEquals("T2", list.get(1).getTitle());
    }

    @Test
    void getByIdOk() {
        Author a = new Author(); a.setId(1L); a.setName("A");
        Category c = new Category(); c.setId(2L); c.setName("C");

        Book b = new Book();
        b.setId(42L);
        b.setTitle("T");
        b.setPublicationYear(2010);
        b.setAuthor(a);
        b.setCategory(c);

        when(bookRepository.findById(42L)).thenReturn(Optional.of(b));

        BookResponse r = bookService.getById(42L);

        assertEquals(42L, r.getId());
        assertEquals("T", r.getTitle());
        assertEquals(2010, r.getPublicationYear());
        assertEquals("A", r.getAuthor().getName());
        assertEquals("C", r.getCategory().getName());
    }

    @Test
    void getByIdNotFound() {
        when(bookRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.getById(123L));
    }

    @Test
    void updateSameTitleSameAuthorOk() {
        Long id = 10L;

        Author author = new Author(); author.setId(1L); author.setName("Ryan Holiday");
        Category category = new Category(); category.setId(3L); category.setName("Stoicism");

        Book existing = new Book();
        existing.setId(id);
        existing.setTitle("Same");
        existing.setDescription("old");
        existing.setPublicationYear(2015);
        existing.setAuthor(author);
        existing.setCategory(category);

        BookRequest req = new BookRequest();
        req.setTitle("Same");
        req.setDescription("new");
        req.setPublicationYear(2016);
        req.setAuthorId(1L);
        req.setCategoryId(3L);

        when(bookRepository.findById(id)).thenReturn(Optional.of(existing));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));

        BookResponse result = bookService.update(id, req);

        assertEquals(id, result.getId());
        assertEquals("Same", result.getTitle());
        assertEquals(2016, result.getPublicationYear());
        assertEquals("new", result.getDescription());
    }

    @Test
    void updateTitleChangedOk() {
        Long id = 20L;

        Author author = new Author(); author.setId(1L);
        Category category = new Category(); category.setId(3L);

        Book existing = new Book();
        existing.setId(id);
        existing.setTitle("Old");
        existing.setAuthor(author);
        existing.setCategory(category);

        BookRequest req = new BookRequest();
        req.setTitle("New");
        req.setDescription("x");
        req.setPublicationYear(2017);
        req.setAuthorId(1L);
        req.setCategoryId(3L);

        when(bookRepository.findById(id)).thenReturn(Optional.of(existing));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(bookRepository.countByTitleIgnoreCaseAndAuthor_Id("New", 1L)).thenReturn(0L);

        BookResponse r = bookService.update(id, req);

        assertEquals("New", r.getTitle());
        assertEquals(2017, r.getPublicationYear());
    }

    @Test
    void updateAuthorChangedOk() {
        Long id = 21L;

        Author oldAuthor = new Author(); oldAuthor.setId(1L); oldAuthor.setName("Old");
        Author newAuthor = new Author(); newAuthor.setId(2L); newAuthor.setName("New");
        Category category = new Category(); category.setId(3L); category.setName("Cat");

        Book existing = new Book();
        existing.setId(id);
        existing.setTitle("Title");
        existing.setAuthor(oldAuthor);
        existing.setCategory(category);

        BookRequest req = new BookRequest();
        req.setTitle("Title");
        req.setDescription("d");
        req.setPublicationYear(2020);
        req.setAuthorId(2L);
        req.setCategoryId(3L);

        when(bookRepository.findById(id)).thenReturn(Optional.of(existing));
        when(authorRepository.findById(2L)).thenReturn(Optional.of(newAuthor));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(bookRepository.countByTitleIgnoreCaseAndAuthor_Id("Title", 2L)).thenReturn(0L);

        BookResponse r = bookService.update(id, req);

        assertEquals("Title", r.getTitle());
        assertEquals(2020, r.getPublicationYear());
        assertEquals("New", r.getAuthor().getName());
    }

    @Test
    void updateDuplicateForSameAuthor() {
        Long id = 30L;

        Author author = new Author(); author.setId(1L);
        Category category = new Category(); category.setId(3L);

        Book existing = new Book();
        existing.setId(id);
        existing.setTitle("Old");
        existing.setAuthor(author);
        existing.setCategory(category);

        BookRequest req = new BookRequest();
        req.setTitle("Existing");
        req.setDescription("x");
        req.setPublicationYear(2000);
        req.setAuthorId(1L);
        req.setCategoryId(3L);

        when(bookRepository.findById(id)).thenReturn(Optional.of(existing));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(bookRepository.countByTitleIgnoreCaseAndAuthor_Id("Existing", 1L)).thenReturn(1L);

        assertThrows(DuplicateResourceException.class, () -> bookService.update(id, req));
    }

    @Test
    void updateNotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        BookRequest req = new BookRequest();
        req.setAuthorId(1L);
        req.setCategoryId(2L);

        assertThrows(ResourceNotFoundException.class, () -> bookService.update(999L, req));
    }

    @Test
    void updateAuthorNotFound() {
        Long id = 40L;

        Author author = new Author(); author.setId(1L);
        Category category = new Category(); category.setId(3L);

        Book existing = new Book();
        existing.setId(id);
        existing.setTitle("T");
        existing.setAuthor(author);
        existing.setCategory(category);

        BookRequest req = new BookRequest();
        req.setTitle("T");
        req.setAuthorId(999L);
        req.setCategoryId(3L);

        when(bookRepository.findById(id)).thenReturn(Optional.of(existing));
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.update(id, req));
    }

    @Test
    void updateCategoryNotFound() {
        Long id = 41L;

        Author author = new Author(); author.setId(1L);
        Category category = new Category(); category.setId(3L);

        Book existing = new Book();
        existing.setId(id);
        existing.setTitle("T");
        existing.setAuthor(author);
        existing.setCategory(category);

        BookRequest req = new BookRequest();
        req.setTitle("T");
        req.setAuthorId(1L);
        req.setCategoryId(999L);

        when(bookRepository.findById(id)).thenReturn(Optional.of(existing));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.update(id, req));
    }

    @Test
    void deleteOk() {
        Long id = 50L;
        Book b = new Book(); b.setId(id);

        when(bookRepository.findById(id)).thenReturn(Optional.of(b));

        assertDoesNotThrow(() -> bookService.delete(id));
    }

    @Test
    void deleteNotFound() {
        when(bookRepository.findById(777L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.delete(777L));
    }

    @Test
    void attachFileOk() {
        Long bookId = 1L;
        String filename = "file.pdf";

        Book book = new Book();
        book.setId(bookId);

        FileAsset file = new FileAsset();
        file.setId(10L);
        file.setFilename(filename);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(fileRepo.findAll()).thenReturn(List.of(file));

        BookResponse resp = bookService.attachFile(bookId, filename);

        assertNotNull(resp);
        assertEquals(bookId, resp.getId());
    }

    @Test
    void attachFileBookNotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.attachFile(999L, "x.pdf"));
    }

    @Test
    void attachFileFileNotFound() {
        Long bookId = 2L;
        Book book = new Book();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(fileRepo.findAll()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> bookService.attachFile(bookId, "missing.pdf"));
    }
}
