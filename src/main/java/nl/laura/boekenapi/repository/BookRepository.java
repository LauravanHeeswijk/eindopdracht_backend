package nl.laura.boekenapi.repository;

import java.util.Optional;
import nl.laura.boekenapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    long countByAuthor_Id(Long authorId);
    long countByTitleIgnoreCaseAndAuthor_Id(String title, Long authorId);
    long countByCategory_Id(Long categoryId);

    Optional<Book> findByFileAsset_Filename(String filename);
}
