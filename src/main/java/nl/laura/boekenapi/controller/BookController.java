package nl.laura.boekenapi.controller;

import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/api/books", produces = "application/json")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));

    }
}
