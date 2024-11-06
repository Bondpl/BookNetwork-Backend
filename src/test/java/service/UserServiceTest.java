package service;

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
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UUID userUuid;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userUuid = UUID.randomUUID();

        user = new User();
        user.setUuid(userUuid);
        user.setUsername("test");
        user.setEmail("123@gmail.com");
        user.setPassword("123");
        user.setDescription("test");
        user.setProfilePictureUrl("test");
        user.setRole(Role.USER);
    }

    @Test
    void CreateUser_Successful() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User createdUser = userService.createUser(user);
        assertEquals(user, createdUser);
    }

    @Test
    void createUser_Failed_DuplicateEmailOrUsername() {
        when(userRepository.save(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("User with this username or email already exists", exception.getMessage());
    }

    @Test
    void createUser_Failed_NullUsername() {
        user.setUsername(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("Email, username, and password are required fields and cannot be null", exception.getMessage());
    }

    @Test
    void createUser_Failed_NullEmail() {
        user.setEmail(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("Email, username, and password are required fields and cannot be null", exception.getMessage());
    }

    @Test
    void GetUserById_Successful() {
        when(userRepository.findById(userUuid)).thenReturn(java.util.Optional.of(user));
        User foundUser = userService.getUserById(userUuid);
        assertEquals(user, foundUser);
    }

    @Test
    void GetUserById_NotFound() {
        when(userRepository.findById(userUuid)).thenReturn(java.util.Optional.empty());
        User foundUser = userService.getUserById(userUuid);
        assertNull(foundUser);
    }

    @Test
    void DeleteUser_Successful() {
        userService.deleteUser(userUuid);
        verify(userRepository, times(1)).deleteById(userUuid);
    }

    @Test
    void DeleteUser_NotFound() {
        doThrow(new RuntimeException("User not found")).when(userRepository).deleteById(userUuid);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(userUuid);
        });

        assertEquals("User not found", exception.getMessage());
    }

}


