package com.panda.galleria.service;

import com.panda.galleria.dto.user.UpdateUserRequest;
import com.panda.galleria.dto.user.UserResponse;
import com.panda.galleria.model.User;
import com.panda.galleria.repository.UserRepository;
import com.panda.galleria.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageUtil imageUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService, ImageUtil imageUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageUtil = imageUtil;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        var user = userRepository.findByUsername(username.toLowerCase().trim());
        if(user.isPresent()) {
            return user.get();
        }
        throw new UsernameNotFoundException("Username " + username + " not found");
    }

    public User update(String username, UpdateUserRequest user) {
        Optional<User> oldUser = userRepository.findByUsername(username.toLowerCase().trim());
        if(oldUser.isPresent()) {
            var updatedUser = oldUser.get();
            if(!user.getPassword().isBlank())
                updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            if(!user.getPhoto().isEmpty())
                updatedUser.setPfpUrl(imageUtil.uploadImage(user.getPhoto()));
            return userRepository.save(updatedUser);
        }
        throw new UsernameNotFoundException("Username " + username + " not found");
    }

}
