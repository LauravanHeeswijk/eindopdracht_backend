package nl.laura.boekenapi.service.admin;

import nl.laura.boekenapi.dto.AuthorRequest;
import nl.laura.boekenapi.dto.AuthorResponse;
import nl.laura.boekenapi.exception.DuplicateResourceException;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.AuthorMapper;
import nl.laura.boekenapi.model.Author;
import nl.laura.boekenapi.repository.AuthorRepository;
import nl.laura.boekenapi.repository.BookRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class AdminAuthorService {

    private final AuthorRepository repo;
    private final BookRepository bookRepo;
    private final AuthorMapper mapper;

    public AdminAuthorService(AuthorRepository repo, BookRepository bookRepo, AuthorMapper mapper) {
        this.repo = repo;
        this.bookRepo = bookRepo;
        this.mapper = mapper;
    }

    public List<AuthorResponse> getAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    public AuthorResponse getById(Long id) {
        Author a = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: id=" + id));
        return mapper.toResponse(a);
    }

    public AuthorResponse create(AuthorRequest req) {
        if (repo.existsByNameIgnoreCase(req.getName())) {
            throw new DuplicateResourceException("Author already exists: " + req.getName());
        }
        Author a = mapper.toEntity(req);
        return mapper.toResponse(repo.save(a));
    }

    public AuthorResponse update(Long id, AuthorRequest req) {
        Author a = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: id=" + id));

        if (!a.getName().equalsIgnoreCase(req.getName())
                && repo.existsByNameIgnoreCase(req.getName())) {
            throw new DuplicateResourceException("Author already exists: " + req.getName());
        }

        mapper.updateEntity(a, req);
        return mapper.toResponse(repo.save(a));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Author not found: id=" + id);
        }

        if (bookRepo.existsByAuthor_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete author: books exist for this author");
        }

        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete author: books exist for this author");
        }
    }
}
