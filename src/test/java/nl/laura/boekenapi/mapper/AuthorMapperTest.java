package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.AuthorResponse;
import nl.laura.boekenapi.model.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorMapperTest {

    private final AuthorMapper mapper = new AuthorMapper();

    @Test
    void toResponse_geeftIdEnNaamTerug() {
        Author a = new Author();
        a.setId(1L);
        a.setName("Ryan Holiday");

        AuthorResponse dto = mapper.toResponse(a);

        assertEquals(1L, dto.getId());
        assertEquals("Ryan Holiday", dto.getName());
    }

    @Test
    void toResponse_nullGeeftNull() {
        assertNull(mapper.toResponse(null));
    }
}
