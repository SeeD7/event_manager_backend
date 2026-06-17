package com.zeromus.eventmanager.service;

import com.zeromus.eventmanager.model.dto.SearchUserDto;
import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.model.enums.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.logging.Logger;

import static java.util.Collections.singletonList;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTestIT {

    private static final Logger LOGGER = Logger.getLogger(UserServiceTestIT.class.getName());

    @Autowired
    private UserService service;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            // Lance le script UNE SEULE FOIS pour toute la classe de test
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/user/base.sql"));
        } catch (Exception e) {
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
        }
    }

    @Test
    void getAllUsers_WhenSearchedByRole_ShouldReturnUsers() {
        SearchUserDto search = SearchUserDto.builder().role(singletonList(UserRole.ADMIN)).build();
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserDto> result = service.getAllUsers(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    void getAllUsers_WhenSearchedByMultipleRole_ShouldReturnUsers() {
        SearchUserDto search = SearchUserDto.builder().role(Arrays.asList(UserRole.ADMIN, UserRole.ORGANIZER)).build();
        Pageable pageable = PageRequest.of(2, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserDto> result = service.getAllUsers(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(1);
    }



    @Test
    void getAllUsers_WhenSearchedByRoleAndFirstName_ShouldReturnUsers() {
        SearchUserDto search = SearchUserDto.builder().role(singletonList(UserRole.ADMIN)).firstName("Le").build();
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserDto> result = service.getAllUsers(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    void getAllUsers_WhenSearchedByNameAndFirstName_ShouldReturnUsers() {
        SearchUserDto search = SearchUserDto.builder().lastName("Nad").firstName("Le").build();
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserDto> result = service.getAllUsers(search, pageable);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(2);
    }
}
