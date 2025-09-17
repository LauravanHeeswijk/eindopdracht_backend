package nl.laura.boekenapi.repository;

import nl.laura.boekenapi.model.LibraryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibraryItemRepository extends JpaRepository<LibraryItem, Long> {
    List<LibraryItem> findAllByUser_Id(Long userId);
    Optional<LibraryItem> findByUser_IdAndBook_Id(Long userId, Long bookId);
}

