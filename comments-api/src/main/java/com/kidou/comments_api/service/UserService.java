package com.kidou.comments_api.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kidou.comments_api.enums.RoleName;
import com.kidou.comments_api.exceptions.BusinessException;
import com.kidou.comments_api.model.Role;
import com.kidou.comments_api.model.User;
import com.kidou.comments_api.model.dto.AuthorDTO;
import com.kidou.comments_api.model.dto.UserCreateDTO;
import com.kidou.comments_api.repository.RoleRepository;
import com.kidou.comments_api.repository.UserRepository;

@Service
public class UserService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, CloudinaryService cloudinaryService,
            PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService,
            AuthenticationManager authenticationManager, RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public User createUser(UserCreateDTO userCreateDTO, MultipartFile avatar) {
        User user = modelMapper.map(userCreateDTO, User.class);

        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    throw new BusinessException("Email already exists");
                });

        if (avatar != null && !avatar.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(avatar);
            user.setAvatarUrl(imageUrl);
        }

        Role role = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new BusinessException("Role not found"));
        user.setRoles(List.of(role));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return user;
    }

    public AuthorDTO getAuthenticatedUser(User user) {
        return AuthorDTO.from(user);
    }

}
