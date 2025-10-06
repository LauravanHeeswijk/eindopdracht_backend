package nl.laura.boekenapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    public AuthorRequest() {
    }

    public AuthorRequest(final String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setName(final String name) { this.name = name; }
}
