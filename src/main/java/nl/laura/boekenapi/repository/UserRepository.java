package nl.laura.boekenapi.repository;

import nl.laura.boekenapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
