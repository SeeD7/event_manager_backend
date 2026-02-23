package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.model.entity.User;
import com.zeromus.eventmanager.model.enums.UserRole;
import com.zeromus.eventmanager.model.mapper.UserMapper;
import com.zeromus.eventmanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final User entity = new User(1L, "Havard", "Nadda", "lulu", UserRole.ADMIN, "lulu.trutru@mail.com", "blerg");
    private final UserDto expectedDto = new UserDto(1L, "Havard", "Nadda", "lulu", UserRole.ADMIN, "lulu.trutru@mail.com", "blerg");
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService service;

    @Test
    void getUserById_WhenIdIsOk_ShouldReturnUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(userMapper.toDto(entity)).thenReturn(expectedDto);
        UserDto result = service.getUserById(1L);
        Assertions.assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void getUserById_WhenIdIsUnknown_ShouldReturnException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> service.getUserById(2L));

        String expectedMessage = "User not found with ID: 2";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getUserByMail_WhenMailIsOk_ShouldReturnUser() {
        when(repository.findByEmail("lulu.trutru@mail.com")).thenReturn(Optional.of(entity));
        when(userMapper.toDto(entity)).thenReturn(expectedDto);
        UserDto result = service.getUserByEmail("lulu.trutru@mail.com");
        Assertions.assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void getUserByMail_WhenMailIsUnknown_ShouldReturnException() {
        when(repository.findByEmail("whatever@mail.com")).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> service.getUserByEmail("whatever@mail.com"));

        String expectedMessage = "User not found with email: whatever@mail.com";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
