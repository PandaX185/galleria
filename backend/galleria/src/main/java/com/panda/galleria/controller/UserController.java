package com.panda.galleria.controller;

import com.panda.galleria.dto.UserResponse;
import com.panda.galleria.model.User;
import com.panda.galleria.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String username, @RequestBody User user) throws BadRequestException {
        return ResponseEntity.ok(userService.update(username,user));
    }

}
