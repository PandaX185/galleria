package com.panda.galleria.service;

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

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    public UserResponse update(String username, User user) throws BadRequestException {
        Optional<User> updatedUser = userRepository.findByUsername(username.toLowerCase().trim());
        if(updatedUser.isPresent()) {
            if(!updatedUser.get().getUsername().isBlank() && userRepository.findByUsername(user.getUsername()).isEmpty())
                updatedUser.get().setUsername(user.getUsername());
            else if(!updatedUser.get().getUsername().isBlank())
                throw new BadRequestException("Username " + user.getUsername() + " already exists");
            if(!updatedUser.get().getPassword().isBlank())
                updatedUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
            if(!updatedUser.get().getPfpUrl().isBlank())
                updatedUser.get().setPfpUrl(updatedUser.get().getPfpUrl());
            return userRepository.save(updatedUser.get()).toUserResponse();
        }
        throw new UsernameNotFoundException("Username " + user.getUsername() + " not found");
    }

}
