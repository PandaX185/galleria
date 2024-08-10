package com.panda.galleria.dto.like;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeRequest {
    private String username;
    private Long postId;
}
