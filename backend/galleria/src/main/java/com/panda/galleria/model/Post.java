package com.panda.galleria.model;

import com.panda.galleria.dto.post.PersonalPostResponse;
import com.panda.galleria.dto.post.PostResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String photoUrl;

    private String caption;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likes;

    public PostResponse toPostResponse() {
        return PostResponse
                .builder()
                .id(id)
                .author(user.toUserResponse())
                .caption(caption)
                .likes(likes.stream().map(Like::toLikeResponse).toList())
                .likesCount((long) likes.size())
                .photoUrl(photoUrl)
                .build();
    }

    public PersonalPostResponse toPersonalPostResponse() {
        return PersonalPostResponse
                .builder()
                .id(id)
                .caption(caption)
                .likesCount((long) likes.size())
                .photoUrl(photoUrl)
                .build();
    }
}
