package nl.laura.boekenapi.service;

import java.util.List;
import java.util.stream.Collectors;
import nl.laura.boekenapi.dto.AuthorRequest;
import nl.laura.boekenapi.dto.AuthorResponse;
import nl.laura.boekenapi.exception.DuplicateResourceException;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.AuthorMapper;
import nl.laura.boekenapi.model.Author;
import nl.laura.boekenapi.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(final AuthorRepository repository) {
        this.repository = repository;
    }

    public AuthorResponse create(final AuthorRequest request) {
        if (repository.countByNameIgnoreCase(request.getName()) > 0) {
            throw new DuplicateResourceException("Naam bestaat al: " + request.getName());
        }
        Author entity = AuthorMapper.toEntity(request);
        Author saved = repository.save(entity);
        return AuthorMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AuthorResponse getById(final Long id) {
        Author entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur niet gevonden: " + id));
        return AuthorMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> getAll() {
        return repository.findAll().stream()
                .map(AuthorMapper::toResponse)
                .collect(Collectors.toList());
    }

    public AuthorResponse update(final Long id, final AuthorRequest request) {
        Author entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur niet gevonden: " + id));

        if (!entity.getName().equalsIgnoreCase(request.getName())
                && repository.countByNameIgnoreCase(request.getName()) > 0) {
            throw new DuplicateResourceException("Naam bestaat al: " + request.getName());
        }

        AuthorMapper.updateEntity(entity, request);
        return AuthorMapper.toResponse(entity);
    }

    public void delete(final Long id) {
        Author entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur niet gevonden: " + id));
        repository.delete(entity);
    }
}
