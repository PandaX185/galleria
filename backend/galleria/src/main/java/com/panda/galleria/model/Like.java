package com.panda.galleria.model;

import com.panda.galleria.dto.like.LikeResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "likes")
@Setter
@Getter
public class Like {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public LikeResponse toLikeResponse() {
        return LikeResponse
                .builder()
                .user(user.toUserResponse())
                .build();
    }
}
