package nl.laura.boekenapi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String passwordHash;
    private String displayName;
    private String role; // "USER" of "ADMIN"

    @OneToMany(mappedBy = "user")
    private List<LibraryItem> libraryItems;

    @OneToMany(mappedBy = "user")
    private List<DownloadLog> downloadLogs;

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<LibraryItem> getLibraryItems() { return libraryItems; }
    public void setLibraryItems(List<LibraryItem> libraryItems) { this.libraryItems = libraryItems; }

    public List<DownloadLog> getDownloadLogs() { return downloadLogs; }
    public void setDownloadLogs(List<DownloadLog> downloadLogs) { this.downloadLogs = downloadLogs; }
}
