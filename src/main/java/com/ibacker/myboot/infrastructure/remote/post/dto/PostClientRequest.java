package com.ibacker.myboot.infrastructure.remote.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostClientRequest {
    private String postId;
    private String authorId;
}
