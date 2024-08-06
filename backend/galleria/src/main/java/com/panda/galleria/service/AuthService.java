package com.panda.galleria.service;

import com.panda.galleria.dto.auth.AuthenticationResponse;
import com.panda.galleria.dto.auth.LoginRequest;
import com.panda.galleria.dto.auth.RegisterRequest;
import com.panda.galleria.model.User;
import com.panda.galleria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${server.port}")
    private String SERVER_PORT;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String uploadImage(MultipartFile photo) {
        try {
            String UPLOAD_DIR = "uploads/";
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String uniqueFilename = UUID.randomUUID() + "_" + photo.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + uniqueFilename);
            Files.write(filePath, photo.getBytes());
            return "http://localhost:" + SERVER_PORT + "/" + UPLOAD_DIR + uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload photo", e);
        }
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
            user.setPfpUrl(uploadImage(request.getPhoto()));
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
