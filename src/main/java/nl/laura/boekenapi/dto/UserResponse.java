package nl.laura.boekenapi.dto;

import nl.laura.boekenapi.model.Role;

public class UserResponse {
    private Long id;
    private String email;
    private String displayName;
    private Role role;

    public UserResponse() {}

    public UserResponse(Long id, String email, String displayName, Role role) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
