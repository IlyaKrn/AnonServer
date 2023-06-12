package com.example.anonserver.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "bigint", nullable = false)
    private long id;
    @Column(columnDefinition = "text", nullable = false)
    private String username;
    @Column(columnDefinition = "text", nullable = false)
    private String password;
    @Column(columnDefinition = "boolean", nullable = false)
    private boolean isBanned;
    @Column(columnDefinition = "bigint[]")
    private ArrayList<Long> subscribersIds;
    @Column(columnDefinition = "bytea", nullable = false)
    private ArrayList<Role> roles;

}
