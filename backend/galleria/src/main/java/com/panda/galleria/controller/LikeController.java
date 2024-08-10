package com.panda.galleria.controller;

import com.panda.galleria.dto.like.LikeRequest;
import com.panda.galleria.dto.like.LikeResponse;
import com.panda.galleria.dto.like.PersonalLikesResponse;
import com.panda.galleria.model.Like;
import com.panda.galleria.service.JwtService;
import com.panda.galleria.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;
    private final JwtService jwtService;

    @Autowired
    public LikeController(LikeService likeService, JwtService jwtService) {
        this.likeService = likeService;
        this.jwtService = jwtService;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<LikeResponse>> getLikesByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(
                likeService.getLikesByPostId(postId)
                        .stream()
                        .map(Like::toLikeResponse)
                        .toList()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<List<PersonalLikesResponse>> getLikesByUsername(
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(
                likeService.getLikesByUsername(jwtService.extractUsername(token.substring(7)))
                        .stream()
                        .map(Like::toPersonalLikesResponse)
                        .toList()
        );
    }

    @PostMapping("/{postId}")
    public ResponseEntity<LikeResponse> createLike(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(
                likeService.saveLike(
                        LikeRequest
                                .builder()
                                .postId(postId)
                                .username(jwtService.extractUsername(token.substring(7)))
                                .build()
                ).toLikeResponse()
        );
    }
}
