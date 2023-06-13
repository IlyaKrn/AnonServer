package com.example.anonserver.api.models;

import com.example.anonserver.domain.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminResponse {
    private long id;
    private String username;
    private boolean isBanned;
    private List<Long> subscribersIds;
    private List<Role> roles;
}
