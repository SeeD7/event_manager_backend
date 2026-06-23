package com.zeromus.eventmanager.configuration;

import com.zeromus.eventmanager.model.dto.SecuredUserDto;
import com.zeromus.eventmanager.model.enums.UserRole;
import com.zeromus.eventmanager.service.impl.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@Import({SpringSecurityConfig.class, PasswordConfig.class})
class SpringSecurityConfigMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void publicEndpoints_shouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/user/exists").param("username", "Lulu"))
                .andExpect(status().isOk());

        JsonMapper mapper = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
        SecuredUserDto newDto = new SecuredUserDto(null, "Havard", "Nadda", "Lulu", UserRole.ADMIN, "lulu.trutru@mail.com", "blerg");
        String jsonContent = mapper.writeValueAsString(newDto);

        when(userService.addUser(any())).thenReturn(null);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    void protectedEndpoints_shouldReturn401WhenAnonymous() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "Lulu", roles = {"ADMIN"})
    void protectedEndpoints_shouldBeAccessibleWhenAuthenticated() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void cors_shouldAllowConfiguredOrigin() throws Exception {
        mockMvc.perform(options("/users")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }
}


