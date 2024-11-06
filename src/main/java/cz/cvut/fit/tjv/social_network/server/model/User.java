package cz.cvut.fit.tjv.social_network.server.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid = UUID.randomUUID();

    @Column(unique = true)
    @NotNull(message = "Username is required")
    private String username;

    @Column(unique = true)
    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    private String description;

    private String profilePictureUrl;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is required")
    private Role role;

}
