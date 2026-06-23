package com.zeromus.eventmanager.controller;

import com.zeromus.eventmanager.model.dto.SecuredUserDto;
import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.model.enums.UserRole;
import com.zeromus.eventmanager.model.search.SearchUser;
import com.zeromus.eventmanager.service.IUserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.HttpStatus.*;

@RestController
@CrossOrigin
public class UserController {

    private final IUserService userService;

    public UserController(final IUserService userService) {
        this.userService = userService;
    }

    /**
     * Create - Add a new user
     *
     * @param user An object user
     * @return The user object saved
     */
    @PostMapping("/user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody SecuredUserDto user) {
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
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable final Long id) {
        try {
            return new ResponseEntity<>(userService.getUserById(id), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Read - Get one user by his email
     *
     * @param email The email of the user
     * @return An User object full filled
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @GetMapping("/user/email/{email}")
    public ResponseEntity<UserDto> getUserByMail(@PathVariable final String email) {
        try {
            return new ResponseEntity<>(userService.getUserByEmail(email), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Read - Get one user by his username
     *
     * @param username The username of the user
     * @return An User object full filled
     */
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @GetMapping("/user/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable final String username) {
        try {
            return new ResponseEntity<>(userService.getUserByUsername(username), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Read - Verify if an user exists or not for a username or email
     *
     * @param username The username of the user
     * @param email    The username of the user
     * @return True if it exists, else false
     */
    @GetMapping("/user/exists")
    public ResponseEntity<Boolean> exists(@RequestParam(required = false) final String username, @RequestParam(required = false) final String email) {
        if ((isBlank(username) && isBlank(email)) || (!isBlank(username) && !isBlank(email))) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        if (!isBlank(username)) {
            try {
                userService.getUserByUsername(username);
                return new ResponseEntity<>(Boolean.TRUE, OK);
            } catch (EntityNotFoundException _) {
                return new ResponseEntity<>(Boolean.FALSE, OK);
            } catch (Exception _) {
                return new ResponseEntity<>(BAD_REQUEST);
            }
        }
        if (!isBlank(email)) {
            try {
                userService.getUserByEmail(email);
                return new ResponseEntity<>(Boolean.TRUE, OK);
            } catch (EntityNotFoundException _) {
                return new ResponseEntity<>(Boolean.FALSE, OK);
            } catch (Exception _) {
                return new ResponseEntity<>(BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(BAD_REQUEST);
    }

    /**
     * Read - Get all users
     *
     * @return - An Iterable object of User full filled
     */
    @RolesAllowed({"ADMIN"})
    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> getUsers(SearchUser search, Pageable pageable) {
        try {
            return new ResponseEntity<>(userService.getAllUsersPaged(search, pageable), OK);
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
    @RolesAllowed({"USER", "ORGANIZER", "ADMIN"})
    @PutMapping("/user/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable final Long id, @RequestBody UserDto user) {
        try {
            return new ResponseEntity<>(userService.updateUser(id, user), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Update - Update the role of an existing user
     *
     * @param id   - The id of the user to update
     * @param role - The role to give
     * @return the updated user
     */
    @RolesAllowed({"ADMIN"})
    @PutMapping("/user/role/{id}")
    public ResponseEntity<UserDto> updateRoleUser(@PathVariable final Long id, final UserRole role) {
        try {
            return new ResponseEntity<>(userService.updateRoleUser(id, role), OK);
        } catch (Exception _) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    /**
     * Update - Update the password of an existing user
     *
     * @param id       - The id of the user to update
     * @param password - The password to insert
     * @return the updated user
     */
    @PutMapping("/user/password/{id}")
    public ResponseEntity<Void> updatePasswordUser(@PathVariable final Long id, final String password) {
        try {
            userService.updatePassword(id, password);
            return new ResponseEntity<>(OK);
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
