package nl.laura.boekenapi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "authors", indexes = {
        @Index(name = "idx_authors_name", columnList = "name", unique = true)
})
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // <-- verplicht en uniek
    private String name;

    @OneToMany(mappedBy = "author")
    private List<Book> books;

    public Author() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
}
