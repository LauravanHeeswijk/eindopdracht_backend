// src/main/java/nl/laura/boekenapi/controller/LibraryController.java
package nl.laura.boekenapi.controller;

import nl.laura.boekenapi.dto.LibraryItemResponse;
import nl.laura.boekenapi.service.LibraryService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/me/library")
public class LibraryController {

    private static final String DEV_FALLBACK_EMAIL = "laura@example.com"; // bestaat in data.sql
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    private String resolveEmail(Principal principal, String headerEmail) {
        if (principal != null) return principal.getName();
        if (headerEmail != null && !headerEmail.isBlank()) return headerEmail;
        return DEV_FALLBACK_EMAIL;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<LibraryItemResponse> add(
            @PathVariable Long bookId,
            Principal principal,
            @RequestHeader(value = "X-Dev-User", required = false) String headerEmail) {

        var email = resolveEmail(principal, headerEmail);
        var created = libraryService.addToLibrary(email, bookId);

        // Location: .../api/me/library/{newId}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()              // bv. .../api/me/library/2
                .replacePath("/api/me/library/{id}")  // → .../api/me/library/{id}
                .buildAndExpand(created.getId())      // → .../api/me/library/10
                .toUri();

        return ResponseEntity.created(location).body(created); // 201 + Location
    }

    @GetMapping
    public List<LibraryItemResponse> list(
            Principal principal,
            @RequestHeader(value = "X-Dev-User", required = false) String headerEmail) {
        var email = resolveEmail(principal, headerEmail);
        return libraryService.listMyLibrary(email);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> remove(
            @PathVariable Long bookId,
            Principal principal,
            @RequestHeader(value = "X-Dev-User", required = false) String headerEmail) {
        var email = resolveEmail(principal, headerEmail);
        libraryService.removeFromLibrary(email, bookId);
        return ResponseEntity.noContent().build(); // idempotent
    }
}
