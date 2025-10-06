package nl.laura.boekenapi.controller;

import jakarta.validation.Valid;
import java.util.List;
import nl.laura.boekenapi.dto.AuthorRequest;
import nl.laura.boekenapi.dto.AuthorResponse;
import nl.laura.boekenapi.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/authors", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorsController {

    private final AuthorService service;

    public AuthorsController(AuthorService service) {
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
    public AuthorResponse create(@Valid @RequestBody AuthorRequest request) {
        return service.create(request);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthorResponse update(@PathVariable Long id, @Valid @RequestBody AuthorRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
