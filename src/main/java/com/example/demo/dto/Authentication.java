package com.example.demo.dto;

import com.example.demo.entity.enums.Role;
import lombok.Getter;

@Getter
public class Authentication {

    private final Long id;
    private final Role role;

    public Authentication(Long id, Role role) {
        this.id = id;
        this.role = role;
    }
}
