package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.UserResponse;
import nl.laura.boekenapi.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) return null;
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setDisplayName(user.getDisplayName());
        dto.setRole(user.getRole());
        return dto;
    }
}
