package com.example.anonserver.repositories;

import com.example.anonserver.domain.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsBySecret(String secret);
}
