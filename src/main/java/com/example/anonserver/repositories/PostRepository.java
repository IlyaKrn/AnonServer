package com.example.anonserver.repositories;

import com.example.anonserver.domain.models.PostModel;
import com.example.anonserver.domain.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostModel, Long> {
}
