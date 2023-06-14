package com.example.anonserver.api;

import com.example.anonserver.api.models.comments.CommentAdminResponse;
import com.example.anonserver.api.models.comments.CommentAdminSelfResponse;
import com.example.anonserver.api.models.comments.CommentBaseResponse;
import com.example.anonserver.api.models.comments.CommentBaseSelfResponse;
import com.example.anonserver.api.models.posts.PostAdminResponse;
import com.example.anonserver.api.models.posts.PostAdminSelfResponse;
import com.example.anonserver.api.models.posts.PostBaseResponse;
import com.example.anonserver.api.models.posts.PostBaseSelfResponse;
import com.example.anonserver.domain.models.CommentModel;
import com.example.anonserver.domain.models.PostModel;
import com.example.anonserver.domain.models.Role;
import com.example.anonserver.domain.models.UserModel;
import com.example.anonserver.repositories.CommentRepository;
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
@RequestMapping("api/comments")
public class CommentController {


    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("getById")
    public ResponseEntity<CommentBaseResponse> getById(@RequestParam("id") long id){
        if(commentRepository.existsById(id)){
            CommentModel c = commentRepository.findById(id).get();
            if (!c.isBanned() || !c.isDeleted()){
                return ResponseEntity.ok(new CommentBaseResponse(c.getId(), c.getText(), c.getImagesUrls(), c.getFilesUrls()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("getAdmById")
    public ResponseEntity<CommentAdminResponse> getAdmById(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().contains(Role.ADMIN)) {
            if(commentRepository.existsById(id)){
                CommentModel c = commentRepository.findById(id).get();
                return ResponseEntity.ok(new CommentAdminResponse(c.getId(), c.getAuthorId(), c.getText(), c.getImagesUrls(), c.getFilesUrls(), c.isBanned(), c.isDeleted()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("getSelfById")
    public ResponseEntity<CommentBaseSelfResponse> getSelfById(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(userRepository.existsByUsername(auth.getName()) && commentRepository.existsById(id)) {
            CommentModel c = commentRepository.findById(id).get();
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            if(c.getAuthorId() == u.getId()){
                if(!c.isDeleted()) {
                    return ResponseEntity.ok(new CommentBaseSelfResponse(c.getId(), c.getAuthorId(), c.getText(), c.getImagesUrls(), c.getFilesUrls(), c.isBanned()));
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @GetMapping("getAdmSelfById")
    public ResponseEntity<CommentAdminSelfResponse> getAdmSelfById(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(userRepository.existsByUsername(auth.getName()) && commentRepository.existsById(id)) {
            CommentModel p = commentRepository.findById(id).get();
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            if(p.getAuthorId() == u.getId() && auth.getAuthorities().contains(Role.ADMIN)){
                return ResponseEntity.ok(new CommentAdminSelfResponse(p.getId(), p.getAuthorId(), p.getText(), p.getImagesUrls(), p.getFilesUrls(), p.isBanned(), p.isDeleted()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
