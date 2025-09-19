package nl.laura.boekenapi.mapper;

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
}
