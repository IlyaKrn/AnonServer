package com.example.anonserver.jwt;


import com.example.anonserver.domain.models.Role;
import com.example.anonserver.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";
    @Autowired
    private UserRepository userRepository;

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);

        if (token != null && jwtProvider.validateAccessToken(token)) {
            final Claims claims = jwtProvider.getAccessClaims(token);
            if (userRepository.existsByUsername(claims.getSubject()) && !userRepository.findByUsername(claims.getSubject()).get().isBanned()){
                final JwtAuthentication jwtInfoToken = new JwtAuthentication();
                jwtInfoToken.setAuthenticated(true);
                jwtInfoToken.setFirstName(claims.getSubject());
                Set<Role> roles = new HashSet<>();
                for (int i = 0; i < claims.get("roles", ArrayList.class).toArray().length; i++){
                    roles.add(Role.getFromString((String) claims.get("roles", ArrayList.class).get(i)));
                }
                jwtInfoToken.setRoles(roles);
                SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
            }
        }
        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        if (request.getHeader("Sec-WebSocket-Protocol") != null)
            return request.getHeader("Sec-WebSocket-Protocol");
        return null;
    }

}