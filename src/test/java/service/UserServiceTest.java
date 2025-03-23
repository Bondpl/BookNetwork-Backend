package service;

import cz.cvut.fit.tjv.social_network.server.dto.user.UserDTO;
import cz.cvut.fit.tjv.social_network.server.dto.user.UserIdDTO;
import cz.cvut.fit.tjv.social_network.server.dto.user.UserRegisterDTO;
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
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("test");
        userRegisterDTO.setEmail("123@gmail.com");
        userRegisterDTO.setPassword("123");
        userRegisterDTO.setDescription("test");
        userRegisterDTO.setProfilePictureUrl("test");
        userRegisterDTO.setRole(Role.USER);

        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword("hashedPassword");
        user.setDescription(userRegisterDTO.getDescription());
        user.setProfilePictureUrl(userRegisterDTO.getProfilePictureUrl());
        user.setRole(userRegisterDTO.getRole());

        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO createdUser = userService.createUser(userRegisterDTO);

        assertNotNull(createdUser);
        assertNotNull(createdUser.getUuid());
        assertEquals(userRegisterDTO.getUsername(), createdUser.getUsername());
        assertEquals(userRegisterDTO.getEmail(), createdUser.getEmail());
        assertEquals(userRegisterDTO.getDescription(), createdUser.getDescription());
        assertEquals(userRegisterDTO.getProfilePictureUrl(), createdUser.getProfilePictureUrl());
        assertEquals(userRegisterDTO.getRole(), createdUser.getRole());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_DuplicateEmail() {
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setUsername("test");
        userDTO.setEmail("123@gmail.com");
        userDTO.setPassword("123");
        userDTO.setDescription("test");
        userDTO.setProfilePictureUrl("test");
        userDTO.setRole(Role.USER);

        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        assertThrows(Exceptions.UserEmailAlreadyExistsException.class, () -> {
            userService.createUser(userDTO);
        });
    }

    @Test
    void createUser_DuplicateUsername() {
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setUsername("test");
        userDTO.setEmail("123@gmail.com");
        userDTO.setPassword("123");
        userDTO.setDescription("test");
        userDTO.setProfilePictureUrl("test");
        userDTO.setRole(Role.USER);

        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        assertThrows(Exceptions.UserUsernameAlreadyExistsException.class, () -> {
            userService.createUser(userDTO);
        });
    }

    @Test
    void getUserById_Found() {
        UserIdDTO userIdDTO = new UserIdDTO();
        UUID uuid = UUID.randomUUID();
        userIdDTO.setUuid(uuid);

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
        UserIdDTO userIdDTO = new UserIdDTO();
        userIdDTO.setUuid(uuid);
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        Exceptions.UserNotFoundException exception = assertThrows(Exceptions.UserNotFoundException.class, () -> userService.getUserById(uuid));
        assertEquals("User with UUID " + uuid + " does not exist", exception.getMessage());
        verify(userRepository, times(1)).findById(uuid);
    }

    @Test
    void createUsers_Successful() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setEmail("123@gmail.com");
        userDTO.setPassword("123");
        userDTO.setDescription("test");
        userDTO.setProfilePictureUrl("test");
        userDTO.setRole(Role.USER);


        UserDTO userDTO2 = new UserDTO();
        userDTO2.setUsername("tesst");
        userDTO2.setEmail("12d3@gmail.com");
        userDTO2.setPassword("123");
        userDTO2.setDescription("test");
        userDTO2.setProfilePictureUrl("test");
        userDTO2.setRole(Role.USER);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        List<User> users = userService.createUsers(Arrays.asList(userDTO, userDTO2));

        assertEquals(2, users.size());
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void deleteUser_Successful() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setUuid(uuid);
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        UserIdDTO userIdDTO = new UserIdDTO();
        userIdDTO.setUuid(uuid);
        User deletedUser = userService.deleteUser(userIdDTO);


        assertEquals(uuid, deletedUser.getUuid());
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_UserNotFound() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());
        UserIdDTO userIdDTO = new UserIdDTO();
        userIdDTO.setUuid(uuid);
        Exceptions.UserNotFoundException exception = assertThrows(Exceptions.UserNotFoundException.class, () -> userService.deleteUser(userIdDTO));

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
