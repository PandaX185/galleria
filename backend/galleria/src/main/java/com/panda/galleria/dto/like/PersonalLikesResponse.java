package com.panda.galleria.dto.like;

import com.panda.galleria.dto.post.PersonalPostResponse;
import com.panda.galleria.dto.post.PostResponse;
import com.panda.galleria.dto.user.UserResponse;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalLikesResponse {
    private UserResponse user;
    private PersonalPostResponse post;
}
