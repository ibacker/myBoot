package com.ibacker.myboot.infrastructure.remote.post.dto;

import lombok.Data;

@Data
public class PostClientDTO {
    private String id;
    private String title;
    private String content;
    private String authorName;
    private int authorId;
    private String version;
    private int score;
}
