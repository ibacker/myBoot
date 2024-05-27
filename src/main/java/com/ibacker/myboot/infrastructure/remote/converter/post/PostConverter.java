package com.ibacker.myboot.infrastructure.remote.converter.post;

import com.ibacker.myboot.domain.resttemplate.entity.Post;
import com.ibacker.myboot.infrastructure.remote.post.dto.PostClientDTO;

public class PostConverter {
    public static Post toPost(PostClientDTO dto) {
        if (dto == null) {
            return null;
        }
        Post post = new Post();
        post.setId(Integer.parseInt(dto.getId()));
        post.setTitle(dto.getTitle());
        post.setBody(dto.getContent());
        post.setUserId(dto.getAuthorId());
        return post;
    }
}
