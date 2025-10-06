package nl.laura.boekenapi.repository;

import nl.laura.boekenapi.model.LibraryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LibraryItemRepository extends JpaRepository<LibraryItem, Long> {

    long countByUserIdAndBookId(Long userId, Long bookId);

    Optional<LibraryItem> findByUserIdAndBookId(Long userId, Long bookId);

    List<LibraryItem> findAllByUserId(Long userId);
}