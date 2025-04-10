package cz.cvut.fit.tjv.social_network.server.service;

import cz.cvut.fit.tjv.social_network.server.dto.user.UserDTO;
import cz.cvut.fit.tjv.social_network.server.dto.user.UserIdDTO;
import cz.cvut.fit.tjv.social_network.server.dto.user.UserRegisterDTO;
import cz.cvut.fit.tjv.social_network.server.exceptions.Exceptions;
import cz.cvut.fit.tjv.social_network.server.model.Role;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public static UserDTO getUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUuid(user.getUuid());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setDescription(user.getDescription());
        userDTO.setProfilePictureUrl(user.getProfilePictureUrl());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO createUser(UserRegisterDTO userRegisterDTO) {

        if (userRegisterDTO.getEmail() == null) {
            throw new IllegalArgumentException("Email is required");
        }

        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new Exceptions.UserEmailAlreadyExistsException("User with this email already exists: " + userRegisterDTO.getEmail());
        }

        if (userRegisterDTO.getUsername() == null) {
            throw new IllegalArgumentException("Username is required");
        }

        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new Exceptions.UserUsernameAlreadyExistsException("User with this username already exists: " + userRegisterDTO.getUsername());
        }
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setDescription(userRegisterDTO.getDescription());
        user.setProfilePictureUrl(userRegisterDTO.getProfilePictureUrl());
        user.setRole(Role.USER);
        userRepository.save(user);
        return mapToDTO(user);
    }

    private UserDTO mapToDTO(User user) {
        return getUserDTO(user);
    }

    public User getUserById(UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new Exceptions.UserNotFoundException("User with UUID " + uuid + " does not exist"));
    }

    public List<User> createUsers(List<UserDTO> userDTOS) {
        return userDTOS.stream()
                .map(userDTO -> {
                    if (userDTO.getEmail() == null || userDTO.getUsername() == null || userDTO.getPassword() == null) {
                        throw new Exceptions.InvalidRequestException("Email, username, and password are required fields and cannot be null");
                    }

                    if (userRepository.existsByEmail(userDTO.getEmail())) {
                        throw new Exceptions.UserEmailAlreadyExistsException("User with this email already exists: " + userDTO.getEmail());
                    }

                    if (userRepository.existsByUsername(userDTO.getUsername())) {
                        throw new Exceptions.UserUsernameAlreadyExistsException("User with this username already exists: " + userDTO.getUsername());
                    }

                    User user = new User();
                    user.setUsername(userDTO.getUsername());
                    user.setEmail(userDTO.getEmail());
                    user.setDescription(userDTO.getDescription());
                    user.setProfilePictureUrl(userDTO.getProfilePictureUrl());
                    user.setRole(Role.USER);

                    String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
                    user.setPassword(hashedPassword);

                    return userRepository.save(user);
                })
                .collect(Collectors.toList());
    }


    public User deleteUser(UserIdDTO userIdDTO) {
        UUID uuid = userIdDTO.getUuid();
        return userRepository.findById(uuid).map(user -> {
            userRepository.delete(user);
            return user;
        }).orElseThrow(() -> new Exceptions.UserNotFoundException("User with UUID " + uuid + " does not exist"));
    }

    public User updateUser(User user) {
        UUID uuid = user.getUuid();
        if (!userRepository.existsById(uuid)) {
            throw new Exceptions.UserNotFoundException("User with UUID " + uuid + " does not exist");
        }
        return userRepository.save(user);
    }

    public UserDTO getUserByEmail(@NotBlank(message = "Email is required") String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exceptions.UserNotFoundException("User with email " + email + " does not exist"));
        return mapToDTO(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }


    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // Retrieve the user's UUID from the session
        String userEmail = authentication.getName();

        // Ensure the user is authenticated
        if (userEmail == null) {
            throw new IllegalStateException("User is not authenticated.");
        }
        // Retrieve the user from the database
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new Exceptions.UserNotFoundException("User with email " + userEmail + " does not exist"));

        return user;
    }

}