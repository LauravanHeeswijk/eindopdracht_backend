package nl.laura.boekenapi;

import nl.laura.boekenapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackendEindopdrachtApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendEindopdrachtApplication.class, args);
    }

    @Bean
    CommandLineRunner authDebug(UserRepository repo, PasswordEncoder enc) {
        return args -> repo.findByEmailIgnoreCase("laura@example.com")
                .ifPresent(u -> {
                    System.out.println("DEBUG user: " + u.getEmail());
                    System.out.println("DEBUG hash len: "
                            + (u.getPasswordHash() == null ? "null" : u.getPasswordHash().length()));
                    System.out.println("DEBUG matches? " + enc.matches("secret", u.getPasswordHash()));
                });
    }
}



