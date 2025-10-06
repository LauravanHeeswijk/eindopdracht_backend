package nl.laura.boekenapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "download_logs")
public class DownloadLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDateTime downloadedAt;

    private String ipAddress;

    private String status;

    public DownloadLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public LocalDateTime getDownloadedAt() { return downloadedAt; }
    public void setDownloadedAt(LocalDateTime downloadedAt) { this.downloadedAt = downloadedAt; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
