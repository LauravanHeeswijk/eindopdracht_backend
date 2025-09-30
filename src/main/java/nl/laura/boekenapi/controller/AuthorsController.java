package nl.laura.boekenapi.controller;

import nl.laura.boekenapi.dto.AuthorResponse;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.AuthorMapper;
import nl.laura.boekenapi.repository.AuthorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorsController {

    private final AuthorRepository repo;
    private final AuthorMapper mapper;

    public AuthorsController(AuthorRepository repo, AuthorMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @GetMapping
    public List<AuthorResponse> all() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public AuthorResponse one(@PathVariable Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + id));
    }
}
