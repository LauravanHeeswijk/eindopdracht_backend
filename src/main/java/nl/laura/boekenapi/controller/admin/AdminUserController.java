package nl.laura.boekenapi.controller.admin;

import jakarta.validation.Valid;
import nl.laura.boekenapi.dto.UserCreateRequest;
import nl.laura.boekenapi.dto.UserResponse;
import nl.laura.boekenapi.dto.UserUpdateRequest;
import nl.laura.boekenapi.service.admin.AdminUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService service;

    public AdminUserController(AdminUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserResponse getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserCreateRequest req) {
        return service.create(req);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
