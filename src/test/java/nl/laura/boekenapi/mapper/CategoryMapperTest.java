package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.model.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private final CategoryMapper mapper = new CategoryMapper();

    @Test
    void toResponse_geeftIdEnNaamTerug() {
        Category c = new Category();
        c.setId(10L);
        c.setName("Programming");

        CategoryResponse dto = mapper.toResponse(c);

        assertEquals(10L, dto.getId());
        assertEquals("Programming", dto.getName());
    }

    @Test
    void toResponse_nullGeeftNull() {
        assertNull(mapper.toResponse(null));
    }
}
