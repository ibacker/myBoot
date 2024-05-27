package com.ibacker.myboot.infrastructure.remote.post;

import com.ibacker.myboot.infrastructure.remote.post.dto.PostClientDTO;

import java.util.List;

public interface PostRequestSource {
    List<PostClientDTO> getPostByPostId(String postId);
}
