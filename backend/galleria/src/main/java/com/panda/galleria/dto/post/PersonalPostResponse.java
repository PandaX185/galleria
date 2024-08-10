package com.panda.galleria.dto.post;

import com.panda.galleria.dto.like.LikeResponse;
import com.panda.galleria.dto.user.UserResponse;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PersonalPostResponse {
    private Long id;
    private String caption;
    private String photoUrl;
    private Long likesCount;
}
