package nl.laura.boekenapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.model.FileAsset;
import nl.laura.boekenapi.model.User;
import nl.laura.boekenapi.repository.BookRepository;
import nl.laura.boekenapi.repository.UserRepository;
import nl.laura.boekenapi.service.DownloadLogService;
import nl.laura.boekenapi.service.LibraryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/books")
public class BookFileController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LibraryService libraryService;
    private final DownloadLogService downloadLogService;
    private final String uploadDir;

    public BookFileController(BookRepository bookRepository,
                              UserRepository userRepository,
                              LibraryService libraryService,
                              DownloadLogService downloadLogService,
                              @Value("${my.upload_location}") String uploadDir) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.libraryService = libraryService;
        this.downloadLogService = downloadLogService;
        this.uploadDir = uploadDir;
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> downloadBookFile(
            @PathVariable Long id,
            Authentication auth,
            HttpServletRequest request) throws Exception {

        // Boek ophalen
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        FileAsset asset = book.getFileAsset();
        if (asset == null) {
            throw new ResourceNotFoundException("Book has no file");
        }

        // Gebruiker ophalen
        User user = userRepository.findByEmailIgnoreCase(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + auth.getName()));

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) clientIp = request.getRemoteAddr();

        if (!isAdmin && !libraryService.hasItem(user.getEmail(), book.getId())) {
            downloadLogService.log(user, book, clientIp, "FORBIDDEN");
            return ResponseEntity.status(403).build();
        }

        Path path = Paths.get(uploadDir).resolve(asset.getStoragePath()).normalize();
        UrlResource resource = new UrlResource(path.toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename(asset.getFileName())
                .build());

        downloadLogService.log(user, book, clientIp, "OK");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
