package nl.laura.boekenapi.repository;

import nl.laura.boekenapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByAuthor_Id(Long authorId);
    boolean existsByTitleIgnoreCaseAndAuthor_Id(String title, Long authorId);
    boolean existsByCategory_Id(Long categoryId);
}
