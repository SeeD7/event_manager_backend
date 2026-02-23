package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.configuration.PasswordConfig;
import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.model.entity.User;
import com.zeromus.eventmanager.model.mapper.UserMapper;
import com.zeromus.eventmanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordConfig passwordConfig;

    public UserService(final UserRepository userRepository, final UserMapper userMapper, final PasswordConfig passwordConfig) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordConfig = passwordConfig;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.get().getUsername())
                    .password(user.get().getPassword())
                    .roles(user.get().getRole().name())
                    .build();
        } else {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
    }

    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    public UserDto getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    public UserDto getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    public Iterable<UserDto> getAllUsers() {
        return userMapper.toIterableDto(userRepository.findAll());
    }

    public UserDto addUser(UserDto user) {
        user.setPassword(passwordConfig.passwordEncoder().encode(user.getPassword()));
        User newUser = userMapper.toEntity(user);
        return userMapper.toDto(userRepository.save(newUser));
    }

    public UserDto updateUser(Long id, UserDto user) {
        Optional<User> e = userRepository.findById(id);
        if (e.isPresent()) {
            User currentUser = e.get();

            String firstName = user.getFirstName();
            if (firstName != null) {
                currentUser.setFirstName(firstName);
            }
            String lastName = user.getLastName();
            if (lastName != null) {
                currentUser.setLastName(lastName);
            }
            String email = user.getEmail();
            if (email != null) {
                currentUser.setEmail(email);
            }
            String password = user.getPassword();
            if (password != null) {
                currentUser.setPassword(password);
            }
            userRepository.save(currentUser);
            return userMapper.toDto(currentUser);
        } else {
            return null;
        }
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }
}
