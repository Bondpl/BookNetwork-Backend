package cz.cvut.fit.tjv.social_network.server.repository;

import cz.cvut.fit.tjv.social_network.server.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByUsername(String username);

    boolean existsByEmail(@NotNull(message = "Email is required") String email);

    Optional<User> findByEmail(@NotNull(message = "Email is required") String email);

    boolean existsByUsername(@NotNull(message = "Username is required") String username);
}
