package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.AuthorResponse;
import nl.laura.boekenapi.dto.BookRequest;
import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.model.Author;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.model.Category;

public final class BookMapper {

    private BookMapper() { }

    public static Book toEntity(final BookRequest request, final Author author, final Category category) {
        if (request == null) {
            return null;
        }
        Book entity = new Book();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setPublicationYear(request.getPublicationYear());
        entity.setAuthor(author);
        entity.setCategory(category);
        return entity;
    }

    public static void updateEntity(final Book entity, final BookRequest request,
                                    final Author author, final Category category) {
        if (entity == null || request == null) {
            return;
        }
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setPublicationYear(request.getPublicationYear());
        entity.setAuthor(author);
        entity.setCategory(category);
    }

    public static BookResponse toResponse(final Book entity) {
        if (entity == null) {
            return null;
        }
        BookResponse dto = new BookResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublicationYear(entity.getPublicationYear());

        Author author = entity.getAuthor();
        if (author != null) {
            AuthorResponse authorDto = new AuthorResponse();
            authorDto.setId(author.getId());
            authorDto.setName(author.getName());
            dto.setAuthor(authorDto);
        }

        Category category = entity.getCategory();
        if (category != null) {
            CategoryResponse categoryDto = new CategoryResponse();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            dto.setCategory(categoryDto);
        }

        return dto;
    }
}


