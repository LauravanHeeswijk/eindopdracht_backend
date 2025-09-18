package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.BookRequest;
import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.model.Author;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.model.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {

    private final AuthorMapper authorMapper = new AuthorMapper();
    private final CategoryMapper categoryMapper = new CategoryMapper();
    private final BookMapper mapper = new BookMapper(authorMapper, categoryMapper);

    @Test
    void toEntity_zetEenvoudigeVelden() {
        BookRequest req = new BookRequest();
        req.setTitle("Discipline Is Destiny");
        req.setPublicationYear(2022);

        Author author = new Author(); author.setId(1L); author.setName("Ryan Holiday");
        Category cat = new Category(); cat.setId(10L); cat.setName("Philosophy");

        Book book = mapper.toEntity(req, author, cat);

        assertEquals("Discipline Is Destiny", book.getTitle());
        assertEquals(2022, book.getPublicationYear());
        assertEquals(1L, book.getAuthor().getId());
        assertEquals(10L, book.getCategory().getId());
    }

    @Test
    void toResponse_geeftGenesteDtosTerug() {
        Author a = new Author(); a.setId(1L); a.setName("Ryan Holiday");
        Category c = new Category(); c.setId(10L); c.setName("Philosophy");

        Book b = new Book();
        b.setId(99L);
        b.setTitle("The Obstacle Is the Way");
        b.setPublicationYear(2014);
        b.setAuthor(a);
        b.setCategory(c);

        BookResponse dto = mapper.toResponse(b);

        assertEquals(99L, dto.getId());
        assertEquals("The Obstacle Is the Way", dto.getTitle());
        assertEquals(2014, dto.getPublicationYear());
        assertNotNull(dto.getAuthor());
        assertEquals("Ryan Holiday", dto.getAuthor().getName());
        assertNotNull(dto.getCategory());
        assertEquals("Philosophy", dto.getCategory().getName());
    }
}
