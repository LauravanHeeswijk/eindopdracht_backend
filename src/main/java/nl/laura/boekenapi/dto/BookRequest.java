package nl.laura.boekenapi.dto;

import jakarta.validation.constraints.*;

public class BookRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 10_000)
    private String description;

    @NotNull
    @Min(2000) @Max(2026)
    private Integer publicationYear;

    @NotNull @Positive
    private Long authorId;

    @NotNull @Positive
    private Long categoryId;

    public BookRequest() { }

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
