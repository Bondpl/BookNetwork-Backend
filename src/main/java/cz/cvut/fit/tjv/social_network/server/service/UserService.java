package cz.cvut.fit.tjv.social_network.server.service;

import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor

public class UserService {

    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if (user.getEmail() == null || user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Email, username, and password are required fields and cannot be null");
        }
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("User with this username or email already exists", e);
        }
    }

    public User getUserById(UUID uuid) {
        return userRepository.findById(uuid).orElse(null);
    }

    public List<User> createUsers(List<User> users) {
        if (users.stream().anyMatch(user -> user.getEmail() == null || user.getUsername() == null || user.getPassword() == null)) {
            throw new IllegalArgumentException("Email, username, and password are required fields and cannot be null");
        }
        try {
            return userRepository.saveAll(users);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("User with this username or email already exists");
        }
    }

    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

}