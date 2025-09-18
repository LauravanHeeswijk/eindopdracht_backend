package nl.laura.boekenapi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "file_asset_id")
    private FileAsset fileAsset;

    @OneToMany(mappedBy = "book")
    private List<LibraryItem> libraryItems;

    @OneToMany(mappedBy = "book")
    private List<DownloadLog> downloadLogs;

    public Book() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public FileAsset getFileAsset() { return fileAsset; }
    public void setFileAsset(FileAsset fileAsset) { this.fileAsset = fileAsset; }

    public List<LibraryItem> getLibraryItems() { return libraryItems; }
    public void setLibraryItems(List<LibraryItem> libraryItems) { this.libraryItems = libraryItems; }

    public List<DownloadLog> getDownloadLogs() { return downloadLogs; }
    public void setDownloadLogs(List<DownloadLog> downloadLogs) { this.downloadLogs = downloadLogs; }
}
