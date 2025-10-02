package nl.laura.boekenapi.service;

import nl.laura.boekenapi.dto.UserCreateRequest;
import nl.laura.boekenapi.exception.DuplicateResourceException;
import nl.laura.boekenapi.mapper.UserMapper;
import nl.laura.boekenapi.model.User;
import nl.laura.boekenapi.repository.UserRepository;
import nl.laura.boekenapi.service.admin.AdminUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock UserRepository repo;
    @Mock PasswordEncoder encoder;
    @Mock UserMapper mapper;

    @InjectMocks
    AdminUserService service;

    @Test
    void createDuplicateEmail_throwsException() {
        var req = new UserCreateRequest();
        req.setEmail("test@example.com");
        req.setDisplayName("Test");
        req.setPassword("Secret123!");

        when(repo.findByEmailIgnoreCase("test@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(DuplicateResourceException.class,
                () -> service.create(req));

        verify(repo, never()).save(any());
    }
}
