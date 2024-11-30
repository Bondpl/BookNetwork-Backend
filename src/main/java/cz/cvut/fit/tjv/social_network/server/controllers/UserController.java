package cz.cvut.fit.tjv.social_network.server.controllers;

import cz.cvut.fit.tjv.social_network.server.dto.user.UserDTO;
import cz.cvut.fit.tjv.social_network.server.dto.user.UserIdDTO;
import cz.cvut.fit.tjv.social_network.server.dto.user.UserRegisterDTO;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static cz.cvut.fit.tjv.social_network.server.service.UserService.getUserDTO;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRegisterDTO userDTO) {
        try {
            UserDTO createdUser = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<List<User>> createUsers(@RequestBody List<@Valid UserDTO> userDTOS) {
        try {
            List<User> createdUsers = userService.createUsers(userDTOS);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUsers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@Valid @RequestBody UserIdDTO userIdDTO) {
        try {
            User deletedUser = userService.deleteUser(userIdDTO);
            return ResponseEntity.ok(deletedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<User> getUserById(@PathVariable UUID uuid) {
        try {
            User user = userService.getUserById(uuid);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/self")
    public ResponseEntity<UserDTO> getSelf() {
        try {
            // Get the authenticated user from the SecurityContext and map it to UserDTO
            UserDTO userDTO = mapToDTO((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    private UserDTO mapToDTO(User user) {
        return getUserDTO(user);
    }


}
