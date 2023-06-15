package com.example.anonserver.domain.models;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum ReportType implements GrantedAuthority {
    POST, COMMENT;

    @Override
    public String getAuthority() {
        return name();
    }


    public static ReportType getFromString(String s){
        switch (s){
            case "POST":
                return ReportType.POST;
            case "COMMENT":
                return ReportType.COMMENT;
            default:
                throw new ClassCastException("String " + s + " can not be cast to ReportType");
        }
    }
}
