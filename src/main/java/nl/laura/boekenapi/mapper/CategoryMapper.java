package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.CategoryRequest;
import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    // Bestond al — laat zo
    public CategoryResponse toResponse(Category c) {
        if (c == null) return null;
        var dto = new CategoryResponse();
        dto.setId(c.getId());
        dto.setName(c.getName());
        return dto;
    }

    public Category toEntity(CategoryRequest req) {
        var c = new Category();
        updateEntity(c, req);
        return c;
    }

    public void updateEntity(Category c, CategoryRequest req) {
        c.setName(req.getName().trim());
    }
}
