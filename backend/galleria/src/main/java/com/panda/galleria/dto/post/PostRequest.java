package com.panda.galleria.dto.post;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostRequest {
    private String caption;
    private String photoUrl;
    private String username;
}
