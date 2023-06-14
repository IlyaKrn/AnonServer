package com.example.anonserver.api;

import com.example.anonserver.api.models.users.UserAdminResponse;
import com.example.anonserver.api.models.users.UserAdminSelfResponse;
import com.example.anonserver.api.models.users.UserBaseResponse;
import com.example.anonserver.api.models.users.UserBaseSelfResponse;
import com.example.anonserver.domain.models.CommentModel;
import com.example.anonserver.domain.models.Role;
import com.example.anonserver.domain.models.UserModel;
import com.example.anonserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("getById")
    public ResponseEntity<UserBaseResponse> getById(@RequestParam("id") long id){
        if(userRepository.existsById(id)){
            UserModel u = userRepository.findById(id).get();
            return ResponseEntity.ok(new UserBaseResponse(u.getId(), u.isBanned(), u.getSubscribersIds().size(), u.getRoles()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("getSelf")
    public ResponseEntity<UserBaseSelfResponse> getSelf(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(userRepository.existsByUsername(auth.getName())){
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            return ResponseEntity.ok(new UserBaseSelfResponse(u.getId(), u.getUsername(), u.getPassword(), u.getSubscribersIds().size(), u.getRoles()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("getAdmById")
    public ResponseEntity<UserAdminResponse> getAdmById(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().contains(Role.ADMIN)) {
            if (userRepository.existsById(id)) {
                UserModel u = userRepository.findById(id).get();
                return ResponseEntity.ok(new UserAdminResponse(u.getId(), u.getUsername(), u.isBanned(), u.getSubscribersIds(), u.getRoles()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("getAdmSelf")
    public ResponseEntity<UserAdminSelfResponse> getAdmSelf(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(userRepository.existsByUsername(auth.getName())){
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            return ResponseEntity.ok(new UserAdminSelfResponse(u.getId(), u.getUsername(), u.getPassword(), u.getSubscribersIds(), u.getRoles()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("ban")
    public ResponseEntity ban(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(userRepository.existsById(id) || userRepository.existsByUsername(auth.getName())) {
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            UserModel c = userRepository.findById(id).get();
            if (auth.getAuthorities().contains(Role.ADMIN)) {
                userRepository.save(new UserModel(c.getId(), c.getUsername(), c.getPassword(), true, c.getSubscribersIds(), c.getRoles()));
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("unban")
    public ResponseEntity unban(@RequestParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(userRepository.existsById(id) || userRepository.existsByUsername(auth.getName())) {
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            UserModel c = userRepository.findById(id).get();
            if (auth.getAuthorities().contains(Role.ADMIN)) {
                userRepository.save(new UserModel(c.getId(), c.getUsername(), c.getPassword(), false, c.getSubscribersIds(), c.getRoles()));
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
