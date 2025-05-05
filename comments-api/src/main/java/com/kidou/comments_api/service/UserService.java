package com.kidou.comments_api.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kidou.comments_api.exceptions.BusinessException;
import com.kidou.comments_api.model.User;
import com.kidou.comments_api.model.dto.UserCreateDTO;
import com.kidou.comments_api.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
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

        userRepository.save(user);
        return user;
    }

}
