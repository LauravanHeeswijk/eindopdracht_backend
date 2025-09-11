package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    // Doel: van Category naar CategoryResponse (wat je terugstuurt)
    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }

        CategoryResponse dto = new CategoryResponse();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
