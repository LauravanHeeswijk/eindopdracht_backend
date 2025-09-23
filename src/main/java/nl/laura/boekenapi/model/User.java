package nl.laura.boekenapi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true)
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)   // <-- email verplicht en uniek
    private String email;

    @Column(nullable = false)                  // <-- hash mag niet null zijn
    private String passwordHash;

    @Column(nullable = false)
    private String displayName;

    @Enumerated(EnumType.STRING)               // <-- enum opslag als string
    @Column(nullable = false)
    private Role role;                          // USER of ADMIN

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

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public List<LibraryItem> getLibraryItems() { return libraryItems; }
    public void setLibraryItems(List<LibraryItem> libraryItems) { this.libraryItems = libraryItems; }

    public List<DownloadLog> getDownloadLogs() { return downloadLogs; }
    public void setDownloadLogs(List<DownloadLog> downloadLogs) { this.downloadLogs = downloadLogs; }
}
