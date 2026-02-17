package com.zeromus.eventmanager.model.mapper;

import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto dto);

    UserDto toDto(User entity);

    Iterable<User> toIterableEntity(Iterable<UserDto> dtos);

    Iterable<UserDto> toIterableDto(Iterable<User> entitys);
}
