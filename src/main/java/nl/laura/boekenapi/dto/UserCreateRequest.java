package nl.laura.boekenapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import nl.laura.boekenapi.model.Role;

public class UserCreateRequest {

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 2, max = 100)
    private String displayName;

    @NotNull
    private Role role;

    @NotBlank @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
