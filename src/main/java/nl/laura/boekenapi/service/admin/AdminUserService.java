package nl.laura.boekenapi.service.admin;

import nl.laura.boekenapi.dto.UserCreateRequest;
import nl.laura.boekenapi.dto.UserResponse;
import nl.laura.boekenapi.dto.UserUpdateRequest;
import nl.laura.boekenapi.exception.DuplicateResourceException;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.mapper.UserMapper;
import nl.laura.boekenapi.model.User;
import nl.laura.boekenapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminUserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;

    public AdminUserService(UserRepository repo, PasswordEncoder encoder, UserMapper mapper) {
        this.repo = repo;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    public List<UserResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public UserResponse getById(Long id) {
        User u = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: id=" + id));
        return mapper.toResponse(u);
    }

    public UserResponse create(UserCreateRequest req) {
        if (repo.findByEmailIgnoreCase(req.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists: " + req.getEmail());
        }

        User u = new User();
        u.setEmail(req.getEmail().trim());
        u.setDisplayName(req.getDisplayName().trim());
        u.setRole(req.getRole());
        u.setPasswordHash(encoder.encode(req.getPassword()));

        return mapper.toResponse(repo.save(u));
    }

    public UserResponse update(Long id, UserUpdateRequest req) {
        User u = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: id=" + id));

        if (req.getDisplayName() != null) {
            u.setDisplayName(req.getDisplayName().trim());
        }
        if (req.getRole() != null) {
            u.setRole(req.getRole());
        }
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPasswordHash(encoder.encode(req.getPassword()));
        }

        return mapper.toResponse(repo.save(u));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("User not found: id=" + id);
        }
        repo.deleteById(id);
    }
}
