package com.example.anonserver.services;


import com.example.anonserver.domain.models.Role;
import com.example.anonserver.domain.models.UserModel;
import com.example.anonserver.repositories.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public Optional<UserModel> getByLogin(@NonNull String login) {
        ArrayList<Role> r = new ArrayList<>();
        return userRepository.findAll().stream()
                .filter(user -> login.equals(user.getUsername()))
                .findFirst();
    }

}