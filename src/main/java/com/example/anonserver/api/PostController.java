package com.example.anonserver.api;

import com.example.anonserver.api.models.posts.PostAdminResponse;
import com.example.anonserver.api.models.posts.PostAdminSelfResponse;
import com.example.anonserver.api.models.posts.PostBaseResponse;
import com.example.anonserver.api.models.posts.PostBaseSelfResponse;
import com.example.anonserver.domain.models.PostModel;
import com.example.anonserver.domain.models.Role;
import com.example.anonserver.domain.models.UserModel;
import com.example.anonserver.repositories.PostRepository;
import com.example.anonserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("getById")
    public ResponseEntity<PostBaseResponse> getById(@RequestParam("id") long id){
        if(postRepository.existsById(id)){
            PostModel p = postRepository.findById(id).get();
            return ResponseEntity.ok(new PostBaseResponse(p.getId(), p.getAuthorId(), p.getLikesIds().size(), p.getCommentsIds(), p.getTags(), p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @GetMapping("getAdmById")
    public ResponseEntity<PostAdminResponse> getAdmById(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().contains(Role.ADMIN)) {
            if(postRepository.existsById(id)){
                PostModel p = postRepository.findById(id).get();
                return ResponseEntity.ok(new PostAdminResponse(p.getId(), p.getAuthorId(), p.getLikesIds(), p.getCommentsIds(), p.getTags(), p.isBanned(), p.isDeleted(), p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("getSelfById")
    public ResponseEntity<PostBaseSelfResponse> getSelfById(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(userRepository.existsByUsername(auth.getName()) && postRepository.existsById(id)) {
            PostModel p = postRepository.findById(id).get();
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            if(p.getAuthorId() == u.getId()){
                return ResponseEntity.ok(new PostBaseSelfResponse(p.getId(), p.getAuthorId(), p.getLikesIds().size(), p.getCommentsIds(), p.getTags(), p.isBanned(), p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @GetMapping("getAdmSelfById")
    public ResponseEntity<PostAdminSelfResponse> getAdmSelfById(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(userRepository.existsByUsername(auth.getName()) && postRepository.existsById(id)) {
            PostModel p = postRepository.findById(id).get();
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            if(p.getAuthorId() == u.getId() && auth.getAuthorities().contains(Role.ADMIN)){
                return ResponseEntity.ok(new PostAdminSelfResponse(p.getId(), p.getAuthorId(), p.getLikesIds(), p.getCommentsIds(), p.getTags(), p.isBanned(), p.isDeleted(), p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
