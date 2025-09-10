package nl.laura.boekenapi.dto;

import jakarta.validation.constraints.*;

public class BookUpdateRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    @Size(min = 10, max = 17)
    private String isbn;

    @NotNull
    @Min(2011)
    @Max(2026)
    private Integer publicationYear;

    @NotNull
    @Positive
    private Long authorId;

    @NotNull
    @Min(1) // of @Positive
    private Long categoryId;

    // Getters en setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
