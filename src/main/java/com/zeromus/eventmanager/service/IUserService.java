package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.model.dto.SecuredUserDto;
import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.model.enums.UserRole;
import com.zeromus.eventmanager.model.search.SearchUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    UserDto getUserById(Long id);

    UserDto getUserByUsername(String username);

    UserDto getUserByEmail(String email);

    Page<UserDto> getAllUsersPaged(SearchUser search, Pageable pageable);

    UserDto addUser(SecuredUserDto user);

    UserDto updateUser(Long id, UserDto user);

    UserDto updateRoleUser(Long id, UserRole role);

    void updatePassword(Long id, String newPassword);

    void deleteUser(final Long id);
}
