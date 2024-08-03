package com.panda.galleria.service;

import com.panda.galleria.dto.UpdateUserRequest;
import com.panda.galleria.dto.UserResponse;
import com.panda.galleria.model.User;
import com.panda.galleria.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(user -> {
            userResponses.add(user.toUserResponse());
        });
        return userResponses;
    }

    public UserResponse findByUsername(String username) {
        var user = userRepository.findByUsername(username.toLowerCase().trim());
        if(user.isPresent()) {
            return user.get().toUserResponse();
        }
        throw new UsernameNotFoundException("Username " + username + " not found");
    }

    public UserResponse update(String username, UpdateUserRequest user) {
        Optional<User> updatedUser = userRepository.findByUsername(username.toLowerCase().trim());
        if(updatedUser.isPresent()) {
            if(!user.getPassword().isBlank())
                updatedUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
            if(!user.getPhoto().isEmpty())
                updatedUser.get().setPfpUrl(authService.uploadImage(user.getPhoto()));
            return userRepository.save(updatedUser.get()).toUserResponse();
        }
        throw new UsernameNotFoundException("Username " + username + " not found");
    }

}
