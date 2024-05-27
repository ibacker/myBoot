package com.ibacker.myboot.infrastructure.remote.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ibacker.myboot.infrastructure.remote.post.EmptyStringDeserializer;
import lombok.Data;

@Data
public class PostClientResponse<T> {
    private String code;
    private String msg;
    // 使用自定义反序列化方法，处理 data 为空字符串场景
    @JsonDeserialize(using = EmptyStringDeserializer.class)
    private T data;
}
