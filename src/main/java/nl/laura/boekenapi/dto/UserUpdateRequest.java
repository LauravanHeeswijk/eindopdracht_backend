package nl.laura.boekenapi.dto;

import nl.laura.boekenapi.model.Role;

public class UserUpdateRequest {
    private String displayName;
    private Role role;
    private String password;

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
