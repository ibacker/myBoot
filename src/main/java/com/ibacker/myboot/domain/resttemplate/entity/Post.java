package com.ibacker.myboot.domain.resttemplate.entity;

import lombok.Data;

@Data
public class Post {
    private int userId;
    private int id;
    private String title;
    private String body;
}
