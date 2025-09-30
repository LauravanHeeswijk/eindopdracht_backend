package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.AuthorRequest;
import nl.laura.boekenapi.dto.AuthorResponse;
import nl.laura.boekenapi.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public AuthorResponse toResponse(Author a) {
        if (a == null) return null;
        var dto = new AuthorResponse();
        dto.setId(a.getId());
        dto.setName(a.getName());
        return dto;
    }

    public Author toEntity(AuthorRequest req) {
        var a = new Author();
        updateEntity(a, req);
        return a;
    }

    public void updateEntity(Author a, AuthorRequest req) {
        a.setName(req.getName().trim());
    }
}
