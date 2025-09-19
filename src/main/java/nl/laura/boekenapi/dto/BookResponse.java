package nl.laura.boekenapi.dto;

public class BookResponse {

    private Long id;
    private String title;
    private String description;
    private Integer publicationYear;
    private AuthorResponse author;
    private CategoryResponse category;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public AuthorResponse getAuthor() { return author; }
    public void setAuthor(AuthorResponse author) { this.author = author; }

    public CategoryResponse getCategory() { return category; }
    public void setCategory(CategoryResponse category) { this.category = category; }
}
