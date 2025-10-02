package nl.laura.boekenapi.controller.admin;

import jakarta.validation.Valid;
import nl.laura.boekenapi.dto.CategoryRequest;
import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.service.admin.AdminCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    private final AdminCategoryService service;

    public AdminCategoryController(AdminCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoryResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CategoryResponse getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@Valid @RequestBody CategoryRequest req) {
        return service.create(req);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
