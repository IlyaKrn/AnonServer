package com.example.anonserver.api;

import com.example.anonserver.api.models.UserAdminResponse;
import com.example.anonserver.api.models.UserAdminSelfResponse;
import com.example.anonserver.api.models.UserBaseResponse;
import com.example.anonserver.api.models.UserSelfResponse;
import com.example.anonserver.domain.models.Role;
import com.example.anonserver.domain.models.UserModel;
import com.example.anonserver.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<UserSelfResponse> getSelf(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(userRepository.existsByUsername(auth.getName())){
            UserModel u = userRepository.findByUsername(auth.getName()).get();
            return ResponseEntity.ok(new UserSelfResponse(u.getId(), u.getUsername(), u.getPassword(), u.isBanned(), u.getSubscribersIds().size(), u.getRoles()));
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
            return ResponseEntity.ok(new UserAdminSelfResponse(u.getId(), u.getUsername(), u.getPassword(), u.isBanned(), u.getSubscribersIds(), u.getRoles()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
