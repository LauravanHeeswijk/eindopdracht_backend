package nl.laura.boekenapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import nl.laura.boekenapi.dto.FileResponse;
import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.model.User;
import nl.laura.boekenapi.repository.BookRepository;
import nl.laura.boekenapi.repository.UserRepository;
import nl.laura.boekenapi.service.DownloadLogService;
import nl.laura.boekenapi.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/files", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    private final FileStorageService files;
    private final DownloadLogService downloadLogs;
    private final UserRepository users;
    private final BookRepository books;

    public FileController(FileStorageService files,
                          DownloadLogService downloadLogs,
                          UserRepository users,
                          BookRepository books) {
        this.files = files;
        this.downloadLogs = downloadLogs;
        this.users = users;
        this.books = books;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FileResponse upload(@RequestParam("file") MultipartFile file) {
        String name = files.storeFile(file);

        String downloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/files/")
                .path(name)
                .toUriString();

        String contentType = file.getContentType() == null ? "application/octet-stream" : file.getContentType();
        long size = file.getSize();

        return new FileResponse(name, contentType, size, downloadUri);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Resource> download(@PathVariable String name, Principal principal, HttpServletRequest request) {
        Resource file = files.loadFileAsResource(name);

        if (principal != null) {
            users.findByEmailIgnoreCase(principal.getName()).ifPresent(u ->
                    books.findByFileAsset_Filename(name).ifPresent(b -> {
                        String ip = request.getRemoteAddr();
                        downloadLogs.log(u, b, ip);
                    })
            );
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
