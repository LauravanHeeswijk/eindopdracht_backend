package nl.laura.boekenapi.dto;

import jakarta.validation.constraints.*;
import java.util.Set;

public class BookCreateRequest {
    @NotBlank @Size(max = 200)
    private String title;

    @NotBlank @Size(min = 10, max = 17)
    private String isbn;

    @NotNull @Positive @Min(1400) @Max(2100)
    private Integer publicationYear;

    @NotNull @Positive
    private Long authorId;

    private Set<@Positive Long> categoryIds;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public Set<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(Set<Long> categoryIds) { this.categoryIds = categoryIds; }
}
