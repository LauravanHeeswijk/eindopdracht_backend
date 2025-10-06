package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.CategoryRequest;
import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.model.Category;

public final class CategoryMapper {

    private CategoryMapper() { }

    public static Category toEntity(final CategoryRequest request) {
        if (request == null) {
            return null;
        }
        Category entity = new Category();
        entity.setName(request.getName());
        return entity;
    }

    public static void updateEntity(final Category entity, final CategoryRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setName(request.getName());
    }

    public static CategoryResponse toResponse(final Category entity) {
        if (entity == null) {
            return null;
        }
        CategoryResponse dto = new CategoryResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}




