package com.panda.galleria.model;

import com.panda.galleria.dto.like.LikeResponse;
import com.panda.galleria.dto.like.PersonalLikesResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "likes",
        indexes = {
                @Index(name = "uq_like",columnList = "user_id,post_id",unique = true)
        }
)
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public PersonalLikesResponse toPersonalLikesResponse() {
        return PersonalLikesResponse
                .builder()
                .user(user.toUserResponse())
                .post(post.toPersonalPostResponse())
                .build();
    }
}
