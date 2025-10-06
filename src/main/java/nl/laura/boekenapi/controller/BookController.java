package nl.laura.boekenapi.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import nl.laura.boekenapi.dto.BookRequest;
import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.service.BookService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    private final BookService service;

    public BookController(final BookService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAll() {
        List<BookResponse> result = service.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getOne(@PathVariable final Long id) {
        BookResponse result = service.getById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponse> create(@Valid @RequestBody final BookRequest req) {
        BookResponse created = service.create(req);
        URI location = URI.create("/api/books/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponse> update(@PathVariable final Long id, @Valid @RequestBody final BookRequest req) {
        BookResponse updated = service.update(id, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
