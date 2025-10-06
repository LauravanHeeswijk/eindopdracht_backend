package nl.laura.boekenapi.service;

import java.util.List;
import java.util.stream.Collectors;
import nl.laura.boekenapi.dto.CategoryRequest;
import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.exception.DuplicateResourceException;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.CategoryMapper;
import nl.laura.boekenapi.model.Category;
import nl.laura.boekenapi.repository.BookRepository;
import nl.laura.boekenapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository repository;
    private final BookRepository bookRepository;

    public CategoryService(final CategoryRepository repository,
                           final BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    public CategoryResponse create(final CategoryRequest request) {
        if (repository.countByNameIgnoreCase(request.getName()) > 0) {
            throw new DuplicateResourceException("Naam bestaat al: " + request.getName());
        }
        Category entity = CategoryMapper.toEntity(request);
        Category saved = repository.save(entity);
        return CategoryMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getById(final Long id) {
        Category entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + id));
        return CategoryMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return repository.findAll().stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse update(final Long id, final CategoryRequest request) {
        Category entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + id));

        if (!entity.getName().equalsIgnoreCase(request.getName())
                && repository.countByNameIgnoreCase(request.getName()) > 0) {
            throw new DuplicateResourceException("Naam bestaat al: " + request.getName());
        }

        CategoryMapper.updateEntity(entity, request);
        return CategoryMapper.toResponse(entity);
    }

    public void delete(final Long id) {
        Category entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + id));

        if (bookRepository.countByCategory_Id(id) > 0) {
            throw new DuplicateResourceException("Categorie wordt nog gebruikt door boeken");
        }

        repository.delete(entity);
    }
}
