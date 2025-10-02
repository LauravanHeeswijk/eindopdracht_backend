package nl.laura.boekenapi.controller.admin;

import jakarta.validation.Valid;
import nl.laura.boekenapi.dto.BookRequest;
import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.dto.BookUpdateRequest;
import nl.laura.boekenapi.service.admin.AdminBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/books", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookController {

    private final AdminBookService service;

    public AdminBookController(AdminBookService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public BookResponse getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@Valid @RequestBody BookRequest req) {
        return service.create(req);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
