package com.ibacker.myboot.domain.resttemplate.service;

import com.ibacker.myboot.domain.resttemplate.entity.Post;
import com.ibacker.myboot.infrastructure.remote.converter.post.PostConverter;
import com.ibacker.myboot.infrastructure.remote.post.PostRequestSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostRequestType {

    @Resource
    PostRequestSource postRequestSource;

    public Post queryPostById(String postId) {
        Optional<Post> op =
                postRequestSource.getPostByPostId(postId).stream().map(PostConverter::toPost).collect(Collectors.toList()).stream().findAny();
        return op.orElse(null);
    }
}
