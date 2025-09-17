package nl.laura.boekenapi.controller;

import jakarta.validation.constraints.Positive;
import nl.laura.boekenapi.dto.LibraryItemResponse;
import nl.laura.boekenapi.mapper.LibraryItemMapper;
import nl.laura.boekenapi.model.LibraryItem;
import nl.laura.boekenapi.service.LibraryItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@Validated
public class LibraryController {

    private final LibraryItemService service;
    private final LibraryItemMapper libraryItemMapper;

    public LibraryController(LibraryItemService service, LibraryItemMapper libraryItemMapper) {
        this.service = service;
        this.libraryItemMapper = libraryItemMapper;
    }

    // GET /api/library?userId=1
    @GetMapping
    public List<LibraryItemResponse> list(@RequestParam @Positive Long userId) {
        return service.getLibraryForUser(userId)
                .stream()
                .map(libraryItemMapper::toResponse)
                .toList();
    }

    // POST /api/library/{bookId}?userId=1
    @PostMapping("/{bookId}")
    public ResponseEntity<LibraryItemResponse> add(@PathVariable @Positive Long bookId,
                                                   @RequestParam @Positive Long userId) {
        LibraryItem created = service.addBook(userId, bookId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(libraryItemMapper.toResponse(created));
    }

    // DELETE /api/library/{bookId}?userId=1
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> remove(@PathVariable @Positive Long bookId,
                                       @RequestParam @Positive Long userId) {
        service.removeBook(userId, bookId);
        return ResponseEntity.noContent().build();
    }
}
