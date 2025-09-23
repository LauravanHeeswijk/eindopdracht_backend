package nl.laura.boekenapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "library_items",
        uniqueConstraints = @UniqueConstraint(name = "uk_library_user_book", columnNames = {"user_id", "book_id"})
)
public class LibraryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime addedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)   // <-- verplicht
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)   // <-- verplicht
    private Book book;

    public LibraryItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
}
