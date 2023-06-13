package com.example.anonserver.api.models;

import com.example.anonserver.domain.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSelfResponse {

    private long id;
    private String username;
    private String password;
    private boolean isBanned;
    private int subscribersCount;
    private List<Role> roles;
}
