package com.example.anonserver.api.models.users;

import com.example.anonserver.domain.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBaseSelfResponse {

    private long id;
    private String username;
    private String password;
    private int subscribersCount;
    private List<Role> roles;
}
