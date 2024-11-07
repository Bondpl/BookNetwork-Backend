package cz.cvut.fit.tjv.social_network.server.service;

import cz.cvut.fit.tjv.social_network.server.dto.user.UserIdRequest;
import cz.cvut.fit.tjv.social_network.server.dto.user.UserRequest;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class UserService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserRequest userRequest) {
        if (userRequest.getEmail() == null || userRequest.getUsername() == null || userRequest.getPassword() == null) {
            throw new IllegalArgumentException("Email, username, and password are required fields and cannot be null");
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("User with this username already exists");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setDescription(userRequest.getDescription());
        user.setProfilePictureUrl(userRequest.getProfilePictureUrl());
        user.setRole(userRequest.getRole());

        String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public User getUserById(UUID uuid) {
        return userRepository.findById(uuid).orElse(null);
    }

    public List<User> createUsers(List<UserRequest> userRequests) {
        return userRequests.stream()
                .map(userRequest -> {
                    if (userRequest.getEmail() == null || userRequest.getUsername() == null || userRequest.getPassword() == null) {
                        throw new IllegalArgumentException("Email, username, and password are required fields and cannot be null");
                    }

                    if (userRepository.existsByEmail(userRequest.getEmail())) {
                        throw new RuntimeException("User with this email already exists: " + userRequest.getEmail());
                    }

                    if (userRepository.existsByUsername(userRequest.getUsername())) {
                        throw new RuntimeException("User with this username already exists: " + userRequest.getUsername());
                    }

                    User user = new User();
                    user.setUsername(userRequest.getUsername());
                    user.setEmail(userRequest.getEmail());
                    user.setDescription(userRequest.getDescription());
                    user.setProfilePictureUrl(userRequest.getProfilePictureUrl());
                    user.setRole(userRequest.getRole());

                    String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
                    user.setPassword(hashedPassword);

                    return userRepository.save(user);
                })
                .collect(Collectors.toList());
    }


    public User deleteUser(UserIdRequest userIdRequest) {
        UUID uuid = userIdRequest.getUuid();
        return userRepository.findById(uuid).map(user -> {
            userRepository.delete(user);
            return user;
        }).orElseThrow(() -> new IllegalArgumentException("User with UUID " + uuid + " does not exist"));
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

}