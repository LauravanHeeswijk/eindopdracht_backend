package nl.laura.boekenapi.dto;

import java.util.Set;

public class BookResponse {
    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private AuthorResponse author;
    private Set<CategoryResponse> categories;
    private LibraryItemResponse libraryItem;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    public AuthorResponse getAuthor() { return author; }
    public void setAuthor(AuthorResponse author) { this.author = author; }
    public Set<CategoryResponse> getCategories() { return categories; }
    public void setCategories(Set<CategoryResponse> categories) { this.categories = categories; }
    public LibraryItemResponse getLibraryItem() { return libraryItem; }
    public void setLibraryItem(LibraryItemResponse libraryItem) { this.libraryItem = libraryItem; }
}
