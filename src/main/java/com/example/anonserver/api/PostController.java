package com.example.anonserver.api;

import com.example.anonserver.api.models.edit.EditPostRequest;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
            if(!p.isBanned() || !p.isDeleted()){
                return ResponseEntity.ok(new PostBaseResponse(p.getId(), p.getLikesIds().size(), p.getCommentsIds(), p.getTags(), p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
                if(!p.isDeleted()) {
                    return ResponseEntity.ok(new PostBaseSelfResponse(p.getId(), p.getAuthorId(), p.getLikesIds().size(), p.getCommentsIds(), p.getTags(), p.isBanned(), p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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

    @PostMapping("delete")
    public ResponseEntity delete(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(postRepository.existsById(id) && userRepository.existsByUsername(auth.getName())) {
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            PostModel p = postRepository.findById(id).get();
            if (p.getAuthorId() == u.getId()) {
                postRepository.save(new PostModel(p.getId(), p.getAuthorId(), p.getLikesIds(), p.getCommentsIds(), p.getText(), p.getTags(), p.isBanned(), true, p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("ban")
    public ResponseEntity ban(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(postRepository.existsById(id) && userRepository.existsByUsername(auth.getName())) {
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            PostModel p = postRepository.findById(id).get();
            if (auth.getAuthorities().contains(Role.ADMIN)) {
                postRepository.save(new PostModel(p.getId(), p.getAuthorId(), p.getLikesIds(), p.getCommentsIds(), p.getText(), p.getTags(), true, p.isDeleted(), p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("unban")
    public ResponseEntity unban(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(postRepository.existsById(id) && userRepository.existsByUsername(auth.getName())) {
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            PostModel p = postRepository.findById(id).get();
            if (auth.getAuthorities().contains(Role.ADMIN)) {
                postRepository.save(new PostModel(p.getId(), p.getAuthorId(), p.getLikesIds(), p.getCommentsIds(), p.getText(), p.getTags(), false, p.isDeleted(), p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("edit")
    public ResponseEntity edit(@RequestParam("id") long id, @RequestBody EditPostRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(postRepository.existsById(id) && userRepository.existsByUsername(auth.getName())) {
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            PostModel p = postRepository.findById(id).get();
            if (p.getAuthorId() == u.getId()) {
                postRepository.save(new PostModel(p.getId(), p.getAuthorId(), p.getLikesIds(), p.getCommentsIds(), request.getText(),request.getTags(), p.isBanned(), p.isDeleted(), p.getUploadTime(), true, request.getImagesUrls(), request.getFilesUrls()));
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("create")
    public ResponseEntity create(@RequestBody EditPostRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserModel u = userRepository.findByUsername(auth.getName()).get();
        PostModel p = new PostModel(0, u.getId(), new ArrayList<>(), new ArrayList<>(), request.getText(), request.getTags(), false, false, System.currentTimeMillis(), false, request.getImagesUrls(), request.getFilesUrls());
        postRepository.save(p);
        return ResponseEntity.ok(null);
    }
    @PostMapping("like")
    public ResponseEntity like(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(postRepository.existsById(id) && userRepository.existsByUsername(auth.getName())) {
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            PostModel p = postRepository.findById(id).get();
            if (!p.getLikesIds().contains(u.getId())) {
                p.getLikesIds().add(u.getId());
                postRepository.save(new PostModel(p.getId(), p.getAuthorId(), p.getLikesIds(), p.getCommentsIds(), p.getText(), p.getTags(), p.isBanned(), p.isDeleted(), p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("unlike")
    public ResponseEntity unlike(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(postRepository.existsById(id) && userRepository.existsByUsername(auth.getName())) {
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            PostModel p = postRepository.findById(id).get();
            if (p.getLikesIds().contains(u.getId())) {
                p.getLikesIds().remove(u.getId());
                postRepository.save(new PostModel(p.getId(), p.getAuthorId(), p.getLikesIds(), p.getCommentsIds(), p.getText(), p.getTags(), p.isBanned(), p.isDeleted(), p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
