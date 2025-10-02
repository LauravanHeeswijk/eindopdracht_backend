package nl.laura.boekenapi.controller.admin;

import jakarta.validation.Valid;
import nl.laura.boekenapi.dto.AuthorRequest;
import nl.laura.boekenapi.dto.AuthorResponse;
import nl.laura.boekenapi.service.admin.AdminAuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/authors", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminAuthorController {

    private final AdminAuthorService service;

    public AdminAuthorController(AdminAuthorService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuthorResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AuthorResponse getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponse create(@Valid @RequestBody AuthorRequest req) {
        return service.create(req);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthorResponse update(@PathVariable Long id, @Valid @RequestBody AuthorRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
