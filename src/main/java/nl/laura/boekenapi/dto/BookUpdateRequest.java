package nl.laura.boekenapi.dto;

import jakarta.validation.constraints.*;

public class BookUpdateRequest {

    @Size(min = 1, max = 200, message = "title must be 1..200 characters when provided")
    private String title;

    @Size(max = 10_000, message = "description max 10.000 characters")
    private String description;

    @Min(value = 0, message = "publicationYear must be >= 0")
    @Max(value = 3000, message = "publicationYear must be <= 3000")
    private Integer publicationYear;

    @Positive(message = "authorId must be positive")
    private Long authorId;


    @Positive(message = "categoryId must be positive")
    private Long categoryId;


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
