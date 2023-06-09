package com.example.anonserver.repositories;

import com.example.anonserver.domain.models.CommentModel;
import com.example.anonserver.domain.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentModel, Long> {
}
