package nl.laura.boekenapi.service.admin;

import nl.laura.boekenapi.dto.CategoryRequest;
import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.exception.DuplicateResourceException;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.CategoryMapper;
import nl.laura.boekenapi.model.Category;
import nl.laura.boekenapi.repository.BookRepository;
import nl.laura.boekenapi.repository.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class AdminCategoryService {

    private final CategoryRepository repo;
    private final BookRepository bookRepo;
    private final CategoryMapper mapper;

    public AdminCategoryService(CategoryRepository repo,
                                BookRepository bookRepo,
                                CategoryMapper mapper) {
        this.repo = repo;
        this.bookRepo = bookRepo;
        this.mapper = mapper;
    }

    public List<CategoryResponse> getAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    public CategoryResponse getById(Long id) {
        Category c = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: id=" + id));
        return mapper.toResponse(c);
    }

    public CategoryResponse create(CategoryRequest req) {
        if (repo.existsByNameIgnoreCase(req.getName())) {
            throw new DuplicateResourceException("Category already exists: " + req.getName());
        }
        Category c = mapper.toEntity(req);
        return mapper.toResponse(repo.save(c));
    }

    public CategoryResponse update(Long id, CategoryRequest req) {
        Category c = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: id=" + id));

        if (!c.getName().equalsIgnoreCase(req.getName())
                && repo.existsByNameIgnoreCase(req.getName())) {
            throw new DuplicateResourceException("Category already exists: " + req.getName());
        }

        mapper.updateEntity(c, req);
        return mapper.toResponse(repo.save(c));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Category not found: id=" + id);
        }
        repo.deleteById(id);
    }
}