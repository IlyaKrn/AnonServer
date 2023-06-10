package com.example.anonserver.domain.models;

import lombok.RequiredArgsConstructor;
import org.hibernate.type.EnumType;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
