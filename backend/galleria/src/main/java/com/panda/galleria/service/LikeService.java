package com.panda.galleria.service;

import com.panda.galleria.dto.like.LikeRequest;
import com.panda.galleria.model.Like;
import com.panda.galleria.repository.LikeRepository;
import com.panda.galleria.repository.PostRepository;
import com.panda.galleria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Like saveLike(LikeRequest request) {
        Like like = Like
                .builder()
                .post(postRepository.findById(request.getPostId()).orElse(null))
                .user(userRepository.findByUsername(request.getUsername()).orElse(null))
                .build();

        likeRepository.save(like);
        return like;
    }

    public List<Like> getLikesByPostId(Long postId) {
        return likeRepository.findAllByPostId(postId);
    }

    public List<Like> getLikesByUsername(String username) {
        return likeRepository.findAllByUser_Username(username);
    }
}
