package nl.laura.boekenapi.dto;

import java.time.LocalDateTime;

public class LibraryItemResponse {
    private Long id;
    private Long userId;
    private Long bookId;
    private String bookTitle;
    private LocalDateTime addedAt;

    public LibraryItemResponse() {}

    public LibraryItemResponse(Long id, Long userId, Long bookId, String bookTitle, LocalDateTime addedAt) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.addedAt = addedAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public LocalDateTime getAddedAt() { return addedAt; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
