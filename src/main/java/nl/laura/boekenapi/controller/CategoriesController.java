package nl.laura.boekenapi.controller;

import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.CategoryMapper;
import nl.laura.boekenapi.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final CategoryRepository repo;
    private final CategoryMapper mapper;

    public CategoriesController(CategoryRepository repo, CategoryMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @GetMapping
    public List<CategoryResponse> all() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public CategoryResponse one(@PathVariable Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }
}
