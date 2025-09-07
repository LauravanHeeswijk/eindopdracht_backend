package nl.laura.boekenapi.repository;

import nl.laura.boekenapi.model.LibraryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryItemRepository extends JpaRepository<LibraryItem, Long> {
}
