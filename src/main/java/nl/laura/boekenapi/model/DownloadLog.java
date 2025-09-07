package nl.laura.boekenapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "download_logs")
public class DownloadLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime downloadedAt;
    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;    // ← ook exact 'user'

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public DownloadLog() {}
}
