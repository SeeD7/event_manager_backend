package com.zeromus.eventmanager.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {SpringSecurityConfig.class, PasswordConfig.class})
@Import(SpringSecurityConfigBeansTest.UserServiceTestConfig.class)
class SpringSecurityConfigBeansTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    void securityBeans_shouldBePresent() {
        assertThat(ctx.getBeanNamesForType(org.springframework.security.web.SecurityFilterChain.class)).isNotEmpty();
        assertThat(ctx.getBean(PasswordConfig.class)).isNotNull();
        assertThat(ctx.getBean(AuthenticationManager.class)).isNotNull();
    }

    // Provide a mocked UserService bean so Spring can construct SpringSecurityConfig
    @org.springframework.boot.test.context.TestConfiguration
    static class UserServiceTestConfig {
        @org.springframework.context.annotation.Bean
        public com.zeromus.eventmanager.service.IUserService userService() {
            return org.mockito.Mockito.mock(com.zeromus.eventmanager.service.IUserService.class);
        }
    }
}





