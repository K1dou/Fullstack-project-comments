package com.kidou.comments_api.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kidou.comments_api.model.User;
import com.kidou.comments_api.model.dto.JwtResponseDTO;
import com.kidou.comments_api.model.dto.LoginUserDTO;
import com.kidou.comments_api.repository.UserRepository;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService,
            UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    public JwtResponseDTO login(LoginUserDTO loginUserDTO) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginUserDTO.getEmail(), loginUserDTO.getPassword());

            var auth = authenticationManager.authenticate(authenticationToken);
            User user = (User) auth.getPrincipal();

            String token = jwtTokenService.generateToken(user);
            String refreshToken = jwtTokenService.generateRefreshToken(user);
            return new JwtResponseDTO(token, refreshToken);

        } catch (Exception e) {
            throw new RuntimeException("Credenciais inválidas.");
        }

    }

    public String refreshToken(String refreshToken) {
        if (!jwtTokenService.isTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh token inválido ou expirado.");
        }

        String email = jwtTokenService.getSubjectFromToken(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        String newAccessToken = jwtTokenService.generateToken(user);

        return newAccessToken;
    }

}
