package com.panda.galleria.controller;

import com.panda.galleria.dto.user.UpdateUserRequest;
import com.panda.galleria.dto.user.UserResponse;
import com.panda.galleria.model.User;
import com.panda.galleria.service.JwtService;
import com.panda.galleria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(
                userService.findAll()
                        .stream()
                        .map(User::toUserResponse)
                        .toList()
        );
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username).toUserResponse());
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) {
        return ResponseEntity.ok(userService.update(username,new UpdateUserRequest(password,photo)).toUserResponse());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            @RequestHeader("Authorization") String token
    ) {
        String username = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(userService.findByUsername(username).toUserResponse());
    }
}
