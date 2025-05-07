package com.kidou.comments_api.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.kidou.comments_api.model.User;
import com.kidou.comments_api.model.dto.LoginUserDTO;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    public String login(LoginUserDTO loginUserDTO) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginUserDTO.getEmail(), loginUserDTO.getPassword());

            var auth = authenticationManager.authenticate(authenticationToken);
            User user = (User) auth.getPrincipal();
            return jwtTokenService.generateToken(user);

        } catch (Exception e) {
            throw new RuntimeException("Credenciais inválidas.");
        }

    }

}
