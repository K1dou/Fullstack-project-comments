package com.kidou.comments_api.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.kidou.comments_api.model.User;
import com.kidou.comments_api.model.dto.UserCreateDTO;
import com.kidou.comments_api.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public User createUser(UserCreateDTO userCreateDTO) {
        User user = modelMapper.map(userCreateDTO, User.class);
        userRepository.save(user);
        return user;
    }

}
