package com.panda.galleria.dto.like;

import com.panda.galleria.dto.user.UserResponse;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LikeResponse {
    private UserResponse user;
}
