package com.example.anonserver.api;

import com.example.anonserver.api.models.edit.EditUserRequest;
import com.example.anonserver.api.models.reports.ReportAdminResponse;
import com.example.anonserver.api.models.users.UserAdminResponse;
import com.example.anonserver.api.models.users.UserAdminSelfResponse;
import com.example.anonserver.api.models.users.UserBaseResponse;
import com.example.anonserver.api.models.users.UserBaseSelfResponse;
import com.example.anonserver.domain.models.*;
import com.example.anonserver.repositories.CommentRepository;
import com.example.anonserver.repositories.PostRepository;
import com.example.anonserver.repositories.ReportRepository;
import com.example.anonserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/reports")
public class ReportController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReportRepository reportRepository;

    @GetMapping("getById")
    public ResponseEntity<ReportAdminResponse> getById(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(reportRepository.existsById(id) && userRepository.existsByUsername(auth.getName())) {
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            ReportModel r = reportRepository.findById(id).get();
            return ResponseEntity.ok(new ReportAdminResponse(r.getId(), r.getAuthorId(), r.getText(), r.getType(), r.getResourceId(), r.getUploadTime()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("check")
    public ResponseEntity check(@RequestParam("id") long id, @RequestParam("violation") boolean violation){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().contains(Role.ADMIN)){
            if(reportRepository.existsById(id) && userRepository.existsByUsername(auth.getName())) {
                UserModel u = userRepository.findByUsername(auth.getName()).get();
                ReportModel r = reportRepository.findById(id).get();
                if(violation){
                    switch (r.getType()){
                        case POST:
                            if(postRepository.existsById(r.getResourceId())){
                                PostModel p = postRepository.findById(r.getResourceId()).get();
                                postRepository.save(new PostModel(p.getId(), p.getAuthorId(), p.getLikesIds(), p.getCommentsIds(), p.getText(), p.getTags(), true, p.isDeleted(), p.getUploadTime(), p.isEdited(), p.getImagesUrls(), p.getFilesUrls()));
                                if(userRepository.existsById(p.getId())){
                                    UserModel u1 = userRepository.findById(p.getId()).get();
                                    userRepository.save(new UserModel(u1.getId(), u1.getSecret(), u1.getUsername(), u1.getPassword(), true, u1.getSubscribersIds(), u1.getRoles()));
                                }
                            }
                            break;
                        case COMMENT:
                            if(commentRepository.existsById(r.getResourceId())){
                                CommentModel c = commentRepository.findById(r.getResourceId()).get();
                                commentRepository.save(new CommentModel(c.getId(), c.getAuthorId(), c.getText(), c.getLikesIds(), c.getImagesUrls(), c.getFilesUrls(), c.getUploadTime(), c.isEdited(), true, c.isDeleted()));
                                if(userRepository.existsById(c.getId())){
                                    UserModel u1 = userRepository.findById(c.getId()).get();
                                    userRepository.save(new UserModel(u1.getId(), u1.getSecret(), u1.getUsername(), u1.getPassword(), true, u1.getSubscribersIds(), u1.getRoles()));
                                }
                            }
                            break;
                    }
                }
                reportRepository.save(new ReportModel(r.getId(), r.getAuthorId(), r.getText(), r.getType(), r.getResourceId(), true, violation, r.getUploadTime()));
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
