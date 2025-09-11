package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.AuthorResponse;
import nl.laura.boekenapi.dto.BookCreateRequest;
import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.dto.BookUpdateRequest;
import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.model.Author;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.model.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookMapper {

    private final AuthorMapper authorMapper;
    private final CategoryMapper categoryMapper;

    public BookMapper(AuthorMapper authorMapper, CategoryMapper categoryMapper) {
        this.authorMapper = authorMapper;
        this.categoryMapper = categoryMapper;
    }


    // Doel: Entity -> Response (uitgaand)
    public BookResponse toResponse(Book book) {
        if (book == null) {
            return null;
        }

        BookResponse dto = new BookResponse();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setPublicationYear(book.getPublicationYear());


        AuthorResponse authorDto = authorMapper.toResponse(book.getAuthor());
        CategoryResponse categoryDto = categoryMapper.toResponse(book.getCategory());
        dto.setAuthor(authorDto);
        dto.setCategory(categoryDto);

        return dto;
    }

    public List<BookResponse> toResponseList(List<Book> books) {
        List<BookResponse> result = new ArrayList<>();
        if (books == null) {
            return result;
        }
        for (Book b : books) {
            result.add(toResponse(b));
        }
        return result;
    }

    // Doel: CreateRequest -> NIEUWE Entity (ingaand)
    public Book toEntity(BookCreateRequest req, Author author, Category category) {
        if (req == null) {
            return null;
        }

        Book book = new Book();
        book.setTitle(req.getTitle());
        book.setPublicationYear(req.getPublicationYear());


        book.setAuthor(author);
        book.setCategory(category);

        return book;
    }


    // Doel: UpdateRequest -> BESTAANDE Entity bijwerken (ingaand)
    public void updateEntity(BookUpdateRequest req, Book target, Author author, Category category) {
        if (req == null || target == null) {
            return;
        }

        target.setTitle(req.getTitle());
        target.setPublicationYear(req.getPublicationYear());

        target.setAuthor(author);
        target.setCategory(category);
    }
}
