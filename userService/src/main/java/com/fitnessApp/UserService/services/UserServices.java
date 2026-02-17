package com.fitnessApp.UserService.services;

import com.fitnessApp.UserService.repositories.UserRepository;
import com.fitnessApp.UserService.dto.RegisterRequest;
import com.fitnessApp.UserService.dto.UserResponse;
import com.fitnessApp.UserService.models.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserServices {

    private final UserRepository repository;
    private final ModelMapper modelMapper;


    public UserResponse register(RegisterRequest request) {
        if(repository.existsByEmail(request.getEmail())){
            throw new RuntimeException(("Email already exist"));
        }
//        User user=new User();
//        user.setEmail(request.getEmail());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setPassword(request.getPassword());
//        User savedUser=repository.save(user);
//        UserResponse userResponse= new UserResponse();
//
//        userResponse.setId(savedUser.getId());
//        userResponse.setPassword(savedUser.getPassword());
//        userResponse.setEmail(savedUser.getEmail());
//        userResponse.setFirstName(savedUser.getFirstName());
//        userResponse.setLastName(savedUser.getLastName());
//        userResponse.setCreatedAT(savedUser.getCreatedAT());
//        userResponse.setUpdatedAt(savedUser.getUpdatedAt());
//
//        return userResponse;

        // Convert DTO → Entity
        User user = modelMapper.map(request, User.class);

        // 🔥 HASH PASSWORD BEFORE SAVING
//        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = repository.save(user);

        // Convert Entity → Response DTO
        return modelMapper.map(savedUser, UserResponse.class);
    }

    public UserResponse getUserProfile(String userId) {
        User user=repository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found"));

        return modelMapper.map(user,UserResponse.class);
    }

    public Boolean existByUserId(String userId) {
        log.info("Calling User Service for: {}",userId);
        return repository.existsById(userId);
    }
}
