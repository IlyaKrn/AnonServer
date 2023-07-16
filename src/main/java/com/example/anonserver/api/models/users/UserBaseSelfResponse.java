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
    private long secret;
    private String username;
    private String password;
    private int subscribersCount;
    private boolean isSubscribed;
    private List<Role> roles;
}
