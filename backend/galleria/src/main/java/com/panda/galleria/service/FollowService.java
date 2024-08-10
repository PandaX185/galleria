package com.panda.galleria.service;

import com.panda.galleria.model.Follow;
import com.panda.galleria.model.User;
import com.panda.galleria.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Autowired
    public FollowService(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    public void follow(String followerUsername, String followingUsername) {
        User follower = userService.findByUsername(followerUsername);
        User following = userService.findByUsername(followingUsername);
        Follow follow = Follow
                .builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);
    }

    public List<User> getFollowers(String username) {
        User user = userService.findByUsername(username);
        List<Follow> followers = followRepository.findByFollowingId(user.getId());
        var result = new ArrayList<User>();
        followers.forEach((follow -> {
            result.add(follow.getFollower());
        }));
        return result;
    }

    public List<User> getFollowing(String username) {
        User user = userService.findByUsername(username);
        List<Follow> following = followRepository.findByFollowerId(user.getId());
        var result = new ArrayList<User>();
        following.forEach((follow -> {
            result.add(follow.getFollowing());
        }));
        return result;
    }
}
