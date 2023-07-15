package com.example.anonserver.api;


import com.example.anonserver.domain.models.Role;
import com.example.anonserver.domain.models.UserModel;
import com.example.anonserver.repositories.UserRepository;
import com.example.anonserver.services.AuthService;
import com.example.anonserver.jwt.models.JwtRequest;
import com.example.anonserver.jwt.models.JwtResponse;
import com.example.anonserver.jwt.models.RefreshJwtRequest;
import com.example.anonserver.services.RabbitMQNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RabbitMQNotificationService rabbitMQNotificationService;
    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
        try {
            final JwtResponse token = authService.login(authRequest);
            rabbitMQNotificationService.sendNotification(userRepository.findByUsername(authRequest.getUsername()).get().getId(), "enter in account");
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        try {
            final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        try {
            final JwtResponse token = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("register")
    public ResponseEntity<JwtResponse> register(@RequestBody JwtRequest authRequest) throws AuthException {
        if(!userRepository.existsByUsername(authRequest.getUsername())){
            ArrayList<Role> roles = new ArrayList<>();
            roles.add(Role.USER);
            userRepository.save(new UserModel(0, authRequest.getUsername(), authRequest.getPassword(), false, new ArrayList<>(), roles));
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        try {
            final JwtResponse token = authService.login(authRequest);
            rabbitMQNotificationService.createNotificationQueue(userRepository.findByUsername(authRequest.getUsername()).get().getId());
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}