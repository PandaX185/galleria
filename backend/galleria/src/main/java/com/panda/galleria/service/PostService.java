package com.panda.galleria.service;

import com.panda.galleria.dto.post.PostRequest;
import com.panda.galleria.dto.post.PostResponse;
import com.panda.galleria.exceptions.PostNotFoundException;
import com.panda.galleria.model.Post;
import com.panda.galleria.model.User;
import com.panda.galleria.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public PostResponse save(PostRequest post) {
        Post postEntity = Post
                .builder()
                .user(userService.findByUsername(post.getUsername()))
                .caption(post.getCaption())
                .photoUrl(post.getPhotoUrl())
                .build();

        return postRepository.save(postEntity).toPostResponse();
    }

    public PostResponse getPost(Long id) throws PostNotFoundException {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return post.get().toPostResponse();
        }
        throw new PostNotFoundException("Post Not Found");
    }

    public List<PostResponse> getPostsByUsername(String username) {
        User user = userService.findByUsername(username);
        List<Post> posts = postRepository.findAllByUserId(user.getId());
        return posts.stream().map(Post::toPostResponse).toList();
    }
}
