package nl.laura.boekenapi.service;

import nl.laura.boekenapi.model.User;
import nl.laura.boekenapi.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User u = users.findByEmailIgnoreCase(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + u.getRole().name());
        List<SimpleGrantedAuthority> authorities = List.of(authority);

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),
                u.getPasswordHash(),
                authorities
        );
    }
}
