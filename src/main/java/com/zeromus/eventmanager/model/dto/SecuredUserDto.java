package com.zeromus.eventmanager.model.dto;

import com.zeromus.eventmanager.model.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecuredUserDto extends UserDto {

    @NotNull
    private String password;

    public SecuredUserDto(Long id, String firstName, String lastName, String username, UserRole role, String email, String password) {
        super(id, firstName, lastName, username, role, email);
        this.password = password;
    }
}
