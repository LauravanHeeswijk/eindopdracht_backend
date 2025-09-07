package nl.laura.boekenapi.repository;

import nl.laura.boekenapi.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
