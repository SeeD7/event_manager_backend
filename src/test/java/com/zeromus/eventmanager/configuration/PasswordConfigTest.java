package com.zeromus.eventmanager.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordConfigTest {

    @Test
    void passwordEncoder_shouldBeBCryptAndMatch() {
        PasswordConfig config = new PasswordConfig();
        PasswordEncoder encoder = config.passwordEncoder();

        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);

        String raw = "mySecret";
        String encoded = encoder.encode(raw);

        assertThat(encoder.matches(raw, encoded)).isTrue();
    }
}

