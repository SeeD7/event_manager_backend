package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.configuration.PasswordConfig;
import com.zeromus.eventmanager.model.dto.SecuredUserDto;
import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.model.enums.UserRole;
import com.zeromus.eventmanager.model.mapper.UserMapper;
import com.zeromus.eventmanager.model.search.SearchUser;
import com.zeromus.eventmanager.repository.UserRepository;
import com.zeromus.eventmanager.service.impl.UserService;
import com.zeromus.eventmanager.utils.AssertionUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static com.zeromus.eventmanager.utils.UserUtils.USER_DTO;
import static com.zeromus.eventmanager.utils.UserUtils.USER_ENTITY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordConfig passwordConfig;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService service;

    @Test
    void loadUserByUsername_WhenUsernameIsOk_ShouldReturnUser() {
        when(repository.findByUsername("Lulu")).thenReturn(Optional.of(USER_ENTITY));
        UserDetails result = service.loadUserByUsername("Lulu");
        Assertions.assertThat(result.getUsername()).isEqualTo("Lulu");
    }

    @Test
    void loadUserByUsername_WhenUsernameIsUnknown_ShouldReturnException() {
        when(repository.findByUsername("Blerg")).thenReturn(Optional.empty());
        Exception ex = assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("Blerg"));

        AssertionUtils.assertExceptionMessageContains(ex, "User not found with username: Blerg");
    }

    @Test
    void getUserById_WhenIdIsOk_ShouldReturnUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));
        when(userMapper.toDto(USER_ENTITY)).thenReturn(USER_DTO);
        UserDto result = service.getUserById(1L);
        Assertions.assertThat(result).isEqualTo(USER_DTO);
    }

    @Test
    void getUserById_WhenIdIsUnknown_ShouldReturnException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.getUserById(2L));

        AssertionUtils.assertExceptionMessageContains(ex, "User not found with ID: 2");
    }

    @Test
    void getUserByMail_WhenMailIsOk_ShouldReturnUser() {
        when(repository.findByEmail("lulu.trutru@mail.com")).thenReturn(Optional.of(USER_ENTITY));
        when(userMapper.toDto(USER_ENTITY)).thenReturn(USER_DTO);
        UserDto result = service.getUserByEmail("lulu.trutru@mail.com");
        Assertions.assertThat(result).isEqualTo(USER_DTO);
    }

    @Test
    void getUserByMail_WhenMailIsUnknown_ShouldReturnException() {
        when(repository.findByEmail("whatever@mail.com")).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.getUserByEmail("whatever@mail.com"));

        AssertionUtils.assertExceptionMessageContains(ex, "User not found with email: whatever@mail.com");
    }

    @Test
    void getUserByUsername_WhenUsernameIsOk_ShouldReturnUser() {
        when(repository.findByUsername("Lulu")).thenReturn(Optional.of(USER_ENTITY));
        when(userMapper.toDto(USER_ENTITY)).thenReturn(USER_DTO);
        UserDto result = service.getUserByUsername("Lulu");
        Assertions.assertThat(result).isEqualTo(USER_DTO);
    }

    @Test
    void getUserByUsername_WhenUsernameIsUnknown_ShouldReturnException() {
        when(repository.findByUsername("Blerg")).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.getUserByUsername("Blerg"));

        AssertionUtils.assertExceptionMessageContains(ex, "User not found with username: Blerg");
    }

    @Test
    void getAllUser_ShouldCallRepository() {
        final SearchUser search = new SearchUser();
        final Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        when(repository.findAll(search, pageable)).thenReturn(new PageImpl<>(Collections.singletonList(USER_ENTITY)));
        when(userMapper.toDto(USER_ENTITY)).thenReturn(USER_DTO);
        service.getAllUsersPaged(search, pageable);
        verify(repository, times(1)).findAll(any(), (Pageable) any());
    }

    @Test
    void addUser_ShouldReturnUserAndCallRepository() {
        SecuredUserDto newDto = new SecuredUserDto(null, "Havard", "Nadda", "Lulu", UserRole.ADMIN, "lulu.trutru@mail.com", "blerg");
        when(passwordConfig.passwordEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode(anyString())).thenReturn("blerg");
        when(userMapper.toEntity(newDto)).thenReturn(USER_ENTITY);
        when(userMapper.toDto(USER_ENTITY)).thenReturn(USER_DTO);
        when(repository.save(USER_ENTITY)).thenReturn(USER_ENTITY);
        UserDto result = service.addUser(newDto);
        Assertions.assertThat(result).isEqualTo(USER_DTO);
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateUser_WhenIdIsOk_ShouldReturnUpdatedDtoAndCallRepository() {
        when(repository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));
        UserDto updatedDto = new UserDto(1L, "Ragnar", "Lothbrok", "Rara", UserRole.ADMIN, "ragnar.lothbrok@mail.com");
        when(repository.findByUsername("Rara")).thenReturn(Optional.empty());
        service.updateUser(1L, updatedDto);
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateUser_WhenEmailAlreadyExists_ShouldReturnException() {
        when(repository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));
        UserDto updatedDto = new UserDto(1L, "Ragnar", "Lothbrok", "Rara", UserRole.ADMIN, "lulu.trutru@mail.com");
        when(repository.findByEmail("lulu.trutru@mail.com")).thenReturn(Optional.of(USER_ENTITY));
        Exception ex = assertThrows(EntityExistsException.class, () -> service.updateUser(1L, updatedDto));

        AssertionUtils.assertExceptionMessageContains(ex, "User with email or username already exists");
    }

    @Test
    void updateUser_WhenUsernameAlreadyExists_ShouldReturnException() {
        when(repository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));
        UserDto updatedDto = new UserDto(1L, "Ragnar", "Lothbrok", "Rara", UserRole.ADMIN, "ragnar.lothbrok@mail.com");
        when(repository.findByEmail("ragnar.lothbrok@mail.com")).thenReturn(Optional.empty());
        when(repository.findByUsername("Rara")).thenReturn(Optional.of(USER_ENTITY));
        Exception ex = assertThrows(EntityExistsException.class, () -> service.updateUser(1L, updatedDto));

        AssertionUtils.assertExceptionMessageContains(ex, "User with email or username already exists");
    }

    @Test
    void updateUser_WhenIdIsOkAndNothingChanged_ShouldReturnUpdatedDtoAndCallRepository() {
        when(repository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));
        service.updateUser(1L, USER_DTO);
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateUser_WhenIdIsUnknown_ShouldReturnException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.updateUser(2L, USER_DTO));

        AssertionUtils.assertExceptionMessageContains(ex, "User not found with id: 2");
    }

    @Test
    void updateRoleUser_WhenIdIsOk_ShouldReturnUpdatedDtoAndCallRepository() {
        when(repository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));
        service.updateRoleUser(1L, UserRole.ORGANIZER);
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateRoleUser_WhenIdIsUnknown_ShouldReturnException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EntityNotFoundException.class, () -> service.updateRoleUser(2L, UserRole.USER));

        AssertionUtils.assertExceptionMessageContains(ex, "User not found with id: 2");
    }

    @Test
    void updatePassword_ShouldRepository() {
        when(repository.findById(1L)).thenReturn(Optional.of(USER_ENTITY));
        when(passwordConfig.passwordEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode(anyString())).thenReturn("blerg");
        service.updatePassword(1L, "Blerg");
        verify(repository, times(1)).save(any());
    }

    @Test
    void deleteUser_WhenIdIsOk_ShouldCallRepository() {
        service.deleteUser(1L);
        verify(repository, times(1)).deleteById(any());
    }

    @Test
    void deleteUser_WhenIdIsUnknown_ShouldCallRepository() {
        service.deleteUser(2L);
        verify(repository, times(1)).deleteById(any());
    }
}
