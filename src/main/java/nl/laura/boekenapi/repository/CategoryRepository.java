package nl.laura.boekenapi.repository;

import nl.laura.boekenapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
