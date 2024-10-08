package com.panda.galleria.service;

import com.panda.galleria.dto.auth.AuthenticationResponse;
import com.panda.galleria.dto.auth.LoginRequest;
import com.panda.galleria.dto.auth.RegisterRequest;
import com.panda.galleria.model.User;
import com.panda.galleria.repository.UserRepository;
import com.panda.galleria.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ImageUtil imageUtil;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, ImageUtil imageUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.imageUtil = imageUtil;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.
                builder()
                .username(request.getUsername().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        if(userRepository.findByUsername(request.getUsername().trim().toLowerCase()).isPresent()){
            throw new BadCredentialsException("Username is already in use");
        }

        if(!request.getPhoto().isEmpty()){
            user.setPfpUrl(imageUtil.uploadImage(request.getPhoto()));
        }

        userRepository.save(user);
        var token = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        var user = userRepository.findByUsername(request.getUsername().toLowerCase().trim());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(request.getUsername());
        }
        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new BadCredentialsException(request.getUsername());
        }
        var token = jwtService.generateToken(user.get());
        return AuthenticationResponse
                .builder()
                .token(token)
                .build();
    }
}
