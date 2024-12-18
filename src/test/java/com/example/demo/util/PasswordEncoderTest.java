package com.example.demo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class PasswordEncoderTest {

    @Test
    @DisplayName("PasswordEncoder 비밀번호 암화화")
    void encode(){
        // given
        String password = "teastPassword12";

        // when
        String encodePassword = PasswordEncoder.encode(password);

        // then
        assertThat(PasswordEncoder.encode(password)).isEqualTo(encodePassword);

    }

    @Test
    @DisplayName("PasswordEncoder 비밀번호 검증")
    void matches(){
        // given
        String rawPassword = "teastPassword12";
        String encodePassword = PasswordEncoder.encode(rawPassword);

        // when
        boolean matches = PasswordEncoder.matches(rawPassword, encodePassword);

        // then
        assertThat(matches).isTrue();

    }

}