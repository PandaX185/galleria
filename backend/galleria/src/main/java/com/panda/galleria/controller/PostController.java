package com.panda.galleria.controller;

import com.panda.galleria.dto.post.PostRequest;
import com.panda.galleria.dto.post.PostResponse;
import com.panda.galleria.exceptions.PostNotFoundException;
import com.panda.galleria.model.Post;
import com.panda.galleria.service.JwtService;
import com.panda.galleria.service.PostService;
import com.panda.galleria.util.ImageUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final JwtService jwtService;
    private final ImageUtil imageUtil;

    public PostController(PostService postService, JwtService jwtService, ImageUtil imageUtil) {
        this.postService = postService;
        this.jwtService = jwtService;
        this.imageUtil = imageUtil;
    }

    @GetMapping("")
    public ResponseEntity<List<PostResponse>> getPosts(@RequestParam String username) {
        return ResponseEntity.ok(postService.getPostsByUsername(username));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) throws PostNotFoundException {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PostMapping("")
    public ResponseEntity<PostResponse> createPost(
            @RequestHeader("Authorization") String token,
            @RequestParam("caption") String caption,
            @RequestParam("photo") MultipartFile photo
    ) {
        String username = jwtService.extractUsername(token.substring(7));
        String photoUrl = imageUtil.uploadImage(photo);
        PostRequest post = PostRequest  
                .builder()
                .photoUrl(photoUrl)
                .caption(caption)
                .username(username)
                .build();

        return ResponseEntity.ok(postService.save(post));
    }
}
