package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.AuthorResponse;
import nl.laura.boekenapi.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    // Doel: Author naar AuthorResponse (wat je terugstuurt)
    public AuthorResponse toResponse(Author author) {
        if (author == null) {
            return null;
        }

        AuthorResponse dto = new AuthorResponse();

        dto.setId(author.getId());
        dto.setName(author.getName());

        return dto;
    }
}
