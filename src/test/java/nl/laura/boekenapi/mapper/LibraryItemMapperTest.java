package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.LibraryItemResponse;
import nl.laura.boekenapi.model.LibraryItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryItemMapperTest {

    private final LibraryItemMapper mapper = new LibraryItemMapper();

    @Test
    void toResponse_neemtVeldOver() {
        LibraryItem li = new LibraryItem();
        li.setId(5L);

        LibraryItemResponse dto = mapper.toResponse(li);

        assertEquals(5L, dto.getId());
    }

    @Test
    void toResponse_nullGeeftNull() {
        assertNull(mapper.toResponse(null));
    }
}
