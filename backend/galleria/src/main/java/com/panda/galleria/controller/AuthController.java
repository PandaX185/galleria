package com.panda.galleria.controller;

import com.panda.galleria.dto.AuthenticationResponse;
import com.panda.galleria.dto.LoginRequest;
import com.panda.galleria.dto.RegisterRequest;
import com.panda.galleria.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        return ResponseEntity.ok(authService.register(new RegisterRequest(username, password, photo)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(authService.login(request));
    }
}
