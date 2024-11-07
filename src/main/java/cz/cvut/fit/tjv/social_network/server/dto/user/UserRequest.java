package cz.cvut.fit.tjv.social_network.server.dto.user;

import cz.cvut.fit.tjv.social_network.server.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UserRequest {

    private UUID uuid;

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    private String description;

    private String profilePictureUrl;

    @NotNull(message = "Role is required")
    private Role role;
}
