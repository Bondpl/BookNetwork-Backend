package service;

import cz.cvut.fit.tjv.social_network.server.dto.user.UserIdRequest;
import cz.cvut.fit.tjv.social_network.server.dto.user.UserRequest;
import cz.cvut.fit.tjv.social_network.server.exceptions.Exceptions;
import cz.cvut.fit.tjv.social_network.server.model.Role;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.repository.UserRepository;
import cz.cvut.fit.tjv.social_network.server.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ReturnsAllUsers() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createUser_Successful() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test");
        userRequest.setEmail("123@gmail.com");
        userRequest.setPassword("123");
        userRequest.setDescription("test");
        userRequest.setProfilePictureUrl("test");
        userRequest.setRole(Role.USER);
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        User createdUser = userService.createUser(userRequest);

        assertNotNull(createdUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_DuplicateEmail() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test");
        userRequest.setEmail("123@gmail.com");
        userRequest.setPassword("123");
        userRequest.setDescription("test");
        userRequest.setProfilePictureUrl("test");
        userRequest.setRole(Role.USER);
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(userRequest));
        assertEquals("User with this email already exists", exception.getMessage());
    }

    @Test
    void createUser_DuplicateUsername() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test");
        userRequest.setEmail("123@gmail.com");
        userRequest.setPassword("123");
        userRequest.setDescription("test");
        userRequest.setProfilePictureUrl("test");
        userRequest.setRole(Role.USER);
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userRequest.getUsername())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(userRequest));
        assertEquals("User with this username already exists", exception.getMessage());
    }

    @Test
    void getUserById_Found() {
        UserIdRequest userIdRequest = new UserIdRequest();
        UUID uuid = UUID.randomUUID();
        userIdRequest.setUuid(uuid);

        User user = new User();
        user.setUuid(uuid);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(uuid);

        assertNotNull(foundUser);
        assertEquals(uuid, foundUser.getUuid());
        verify(userRepository, times(1)).findById(uuid);
    }

    @Test
    void getUserById_NotFound() {
        UUID uuid = UUID.randomUUID();
        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUuid(uuid);
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        Exceptions.UserNotFoundException exception = assertThrows(Exceptions.UserNotFoundException.class, () -> userService.getUserById(uuid));
        assertEquals("User with UUID " + uuid + " does not exist", exception.getMessage());
        verify(userRepository, times(1)).findById(uuid);
    }

    @Test
    void createUsers_Successful() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("test");
        userRequest.setEmail("123@gmail.com");
        userRequest.setPassword("123");
        userRequest.setDescription("test");
        userRequest.setProfilePictureUrl("test");
        userRequest.setRole(Role.USER);


        UserRequest userRequest2 = new UserRequest();
        userRequest2.setUsername("tesst");
        userRequest2.setEmail("12d3@gmail.com");
        userRequest2.setPassword("123");
        userRequest2.setDescription("test");
        userRequest2.setProfilePictureUrl("test");
        userRequest2.setRole(Role.USER);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        List<User> users = userService.createUsers(Arrays.asList(userRequest, userRequest2));

        assertEquals(2, users.size());
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void deleteUser_Successful() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setUuid(uuid);
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUuid(uuid);
        User deletedUser = userService.deleteUser(userIdRequest);


        assertEquals(uuid, deletedUser.getUuid());
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_UserNotFound() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());
        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUuid(uuid);
        Exceptions.UserNotFoundException exception = assertThrows(Exceptions.UserNotFoundException.class, () -> userService.deleteUser(userIdRequest));

        assertEquals("User with UUID " + uuid + " does not exist", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void updateUser_Successful() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setUuid(uuid);
        user.setUsername("test");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setDescription("description");
        user.setProfilePictureUrl("url");
        user.setRole(Role.USER);

        when(userRepository.existsById(uuid)).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals("test", updatedUser.getUsername());
        assertEquals("test@example.com", updatedUser.getEmail());
        assertEquals("description", updatedUser.getDescription());
        assertEquals("url", updatedUser.getProfilePictureUrl());
        assertEquals(Role.USER, updatedUser.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_UserNotFound() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setUuid(uuid);

        when(userRepository.existsById(uuid)).thenReturn(false);

        Exceptions.UserNotFoundException exception = assertThrows(Exceptions.UserNotFoundException.class, () -> userService.updateUser(user));
        assertEquals("User with UUID " + uuid + " does not exist", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
