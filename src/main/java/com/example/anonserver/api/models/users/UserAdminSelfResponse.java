package com.example.anonserver.api.models.users;

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
public class UserAdminSelfResponse {

    private long id;
    private long secret;
    private String username;
    private String password;
    private List<Long> subscribersIds;
    private List<Role> roles;
}
