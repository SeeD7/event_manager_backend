package com.zeromus.eventmanager.utils;

import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.model.entity.User;
import com.zeromus.eventmanager.model.enums.UserRole;

public class UserUtils {
    private UserUtils() {}

    public static final User USER_ENTITY = new User(1L, "Havard", "Nadda", "Lulu", UserRole.ADMIN, "lulu.trutru@mail.com", "blerg");
    public static final UserDto USER_DTO = new UserDto(1L, "Havard", "Nadda", "Lulu", UserRole.ADMIN, "lulu.trutru@mail.com");
}
