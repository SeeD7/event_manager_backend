package com.zeromus.eventmanager.model.mapper;

import com.zeromus.eventmanager.model.dto.SecuredUserDto;
import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.model.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Mapping pour sortie : NE PAS exposer le password
    UserDto toDto(User user);

    // Si besoin explicite d'un DTO avec mot de passe (rare pour sortie),
    @Mapping(target = "password", source = "password")
    SecuredUserDto toSecuredDto(User user);

    // Mapping inverse pour entité depuis DTO de sortie -> password ignoré
    @InheritInverseConfiguration(name = "toDto")
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDto dto);

    // Mapping pour création/entrée (Secure DTO contient password)
    @InheritInverseConfiguration(name = "toSecuredDto")
    User toEntity(SecuredUserDto dto);

    Iterable<User> toIterableEntity(Iterable<UserDto> dtos);

    Iterable<UserDto> toIterableDto(Iterable<User> entitys);
}
