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


    public static Role getFromString(String s){
        switch (s){
            case "USER":
                return Role.USER;
            case "ADMIN":
                return Role.ADMIN;
            default:
                throw new ClassCastException("String " + s + " can not be cast to Role");
        }
    }
}
