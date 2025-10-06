package nl.laura.boekenapi.controller;

import nl.laura.boekenapi.dto.LibraryItemResponse;
import nl.laura.boekenapi.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/me/library")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public LibraryItemResponse add(@PathVariable Long bookId, Principal principal) {
        String email = principal.getName();
        return libraryService.addToLibrary(email, bookId);
    }

    @GetMapping
    public List<LibraryItemResponse> list(Principal principal) {
        String email = principal.getName();
        return libraryService.listMyLibrary(email);
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long bookId, Principal principal) {
        String email = principal.getName();
        libraryService.removeFromLibrary(email, bookId);
    }
}
