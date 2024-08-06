package com.panda.galleria.controller;

import com.panda.galleria.dto.user.UserResponse;
import com.panda.galleria.model.Follow;
import com.panda.galleria.model.User;
import com.panda.galleria.service.FollowService;
import com.panda.galleria.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FollowController {

    private final FollowService followService;
    private final JwtService jwtService;

    @Autowired
    public FollowController(FollowService followService, JwtService jwtService) {
        this.followService = followService;
        this.jwtService = jwtService;
    }

    @PostMapping("/follow/{username}")
    public ResponseEntity<Void> follow(
            @RequestHeader("Authorization") String token,
            @PathVariable String username
    ) {
        String myUsername = jwtService.extractUsername(token.substring(7));
        followService.follow(myUsername, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/following/{username}")
    public ResponseEntity<List<UserResponse>> getFollowing(@PathVariable String username) {
        var result = new ArrayList<UserResponse>();
        followService.getFollowing(username).forEach(user -> {
            result.add(user.toUserResponse());
        });
        return ResponseEntity.ok(result);
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable String username) {
        var result = new ArrayList<UserResponse>();
        followService.getFollowers(username).forEach(user -> {
            result.add(user.toUserResponse());
        });
        return ResponseEntity.ok(result);
    }
}
