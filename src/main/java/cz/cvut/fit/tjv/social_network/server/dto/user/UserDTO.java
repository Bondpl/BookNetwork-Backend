package cz.cvut.fit.tjv.social_network.server.dto.user;

import cz.cvut.fit.tjv.social_network.server.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    private UUID uuid;

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String description;

    private String profilePictureUrl;

    @NotNull(message = "Role is required")
    private Role role;
}
