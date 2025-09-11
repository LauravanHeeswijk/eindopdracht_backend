package nl.laura.boekenapi.dto;

public class LibraryItemResponse {
    private Long id;

    private Long bookId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
}
