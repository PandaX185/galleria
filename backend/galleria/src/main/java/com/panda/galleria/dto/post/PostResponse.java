package com.panda.galleria.dto.post;

import com.panda.galleria.dto.like.LikeResponse;
import com.panda.galleria.dto.user.UserResponse;
import com.panda.galleria.model.Like;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostResponse {
    private Long id;
    private String caption;
    private String photoUrl;
    private UserResponse author;
    private List<LikeResponse> likes;
    private Long likesCount;
}
