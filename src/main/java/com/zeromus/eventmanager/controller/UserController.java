package com.zeromus.eventmanager.controller;

import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Create - Add a new user
     *
     * @param user An object user
     * @return The user object saved
     */
    @PostMapping("/user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        try {
            return new ResponseEntity<>(userService.addUser(user), CREATED);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }


    /**
     * Read - Get one user
     *
     * @param id The id of the user
     * @return An User object full filled
     */
    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable final Long id) {
        try {
            return new ResponseEntity<>(userService.getUserById(id), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Read - Get one user by his mail
     *
     * @param mail The mail of the user
     * @return An User object full filled
     */
    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/user/mail/{mail}")
    public ResponseEntity<UserDto> getUserByMail(@PathVariable final Long mail) {
        try {
            return new ResponseEntity<>(userService.getUserById(mail), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Read - Get all users
     *
     * @return - An Iterable object of User full filled
     */
    @RolesAllowed({"ADMIN"})
    @GetMapping("/users")
    public ResponseEntity<Iterable<UserDto>> getUsers() {
        try {
            return new ResponseEntity<>(userService.getAllUsers(), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Update - Update an existing user
     *
     * @param id   - The id of the user to update
     * @param user - The user object updated
     * @return the updated user
     */
    @RolesAllowed({"USER", "ADMIN"})
    @PutMapping("/user/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable final Long id, @RequestBody UserDto user) {
        try {
            return new ResponseEntity<>(userService.updateUser(id, user), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }


    /**
     * Delete - Delete an user
     *
     * @param id - The id of the user to delete
     */
    @RolesAllowed({"ADMIN"})
    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable final Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserDto> currentUser(Authentication authentication) {
        UserDto user = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(user);
    }
}
