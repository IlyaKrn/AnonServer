package com.example.anonserver.repositories;

import com.example.anonserver.domain.models.PostModel;
import com.example.anonserver.domain.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostModel, Long> {
    List<PostModel> findByIsBanned(boolean isBanned);
    List<PostModel> findByAuthorId(long authorId);
}
