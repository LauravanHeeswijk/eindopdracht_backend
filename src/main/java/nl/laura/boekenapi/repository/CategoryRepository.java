package nl.laura.boekenapi.repository;

import nl.laura.boekenapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Category> findByNameIgnoreCase(String name);
}
