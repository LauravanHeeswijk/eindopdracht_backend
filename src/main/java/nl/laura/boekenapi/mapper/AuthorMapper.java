package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.AuthorRequest;
import nl.laura.boekenapi.dto.AuthorResponse;
import nl.laura.boekenapi.model.Author;

public final class AuthorMapper {

    private AuthorMapper() { }

    public static Author toEntity(final AuthorRequest request) {
        if (request == null) {
            return null;
        }
        Author entity = new Author();
        entity.setName(request.getName());
        return entity;
    }

    public static void updateEntity(final Author entity, final AuthorRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setName(request.getName());
    }

    public static AuthorResponse toResponse(final Author entity) {
        if (entity == null) {
            return null;
        }
        AuthorResponse dto = new AuthorResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
