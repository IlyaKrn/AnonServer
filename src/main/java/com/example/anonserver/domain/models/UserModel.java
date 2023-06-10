package com.example.anonserver.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String password;
    private boolean isBanned;
    private ArrayList<Long> subscribersIds;
    @Column(columnDefinition = "bytea")
    private ArrayList<Role> roles;

}
