package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponse toResponse(Category c) {
        if (c == null) return null;
        var dto = new CategoryResponse();

        dto.setId(c.getId());
        dto.setName(c.getName());

        return dto;
    }
}
