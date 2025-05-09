package com.kidou.comments_api.security;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.kidou.comments_api.enums.RoleName;
import com.kidou.comments_api.model.Role;
import com.kidou.comments_api.model.User;
import com.kidou.comments_api.repository.RoleRepository;
import com.kidou.comments_api.repository.UserRepository;
import com.kidou.comments_api.service.JwtTokenService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomOauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOauth2SuccessHandler(JwtTokenService jwtTokenService, UserRepository userRepository,
            RoleRepository roleRepository) {
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = authToken.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String avatar = oAuth2User.getAttribute("picture");

        if (email == null) {
            response.sendRedirect("https://interactive-comments-theta-seven.vercel.app/login?error=email");
            return;
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setNome(name);
            newUser.setAvatarUrl(avatar);
            newUser.setPassword(UUID.randomUUID().toString());
            Role role = roleRepository.findByName(RoleName.ROLE_CUSTOMER).orElseThrow();
            newUser.setRoles(List.of(role));
            return userRepository.save(newUser);
        });
        String jwt = jwtTokenService.generateToken(user);
        response.sendRedirect("https://interactive-comments-theta-seven.vercel.app/oauth2/callback?token=" + jwt);
    }

}
