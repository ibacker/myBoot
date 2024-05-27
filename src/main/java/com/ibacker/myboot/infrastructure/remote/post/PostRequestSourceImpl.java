package com.ibacker.myboot.infrastructure.remote.post;

import com.ibacker.myboot.infrastructure.remote.post.dto.PostClientDTO;
import com.ibacker.myboot.infrastructure.remote.post.dto.PostClientResponse;
import com.ibacker.myboot.infrastructure.remote.post.dto.PostClientRequest;
import com.ibacker.myboot.infrastructure.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class PostRequestSourceImpl implements PostRequestSource {

    @Value("${postRequestClientPath:http://localhost:9999}")
    private String clientPath;

    @Value("${postRequest.jsonObjectUriPath:/json}")
    private String jsonObjectUriPath;

    @Resource
    private RestTemplate restTemplate;

    public List<PostClientDTO> getPostByPostId(String postId) {
        List<PostClientDTO> posts = new ArrayList<>();
        try {
            ParameterizedTypeReference<PostClientResponse<PostClientDTO>> reference = new ParameterizedTypeReference<PostClientResponse<PostClientDTO>>() {};

            PostClientRequest request = PostClientRequest.builder().postId(postId).build();
            HttpEntity<String> entity = new HttpEntity<>(JsonUtil.objectToJson(request));
            ResponseEntity<PostClientResponse<PostClientDTO>> response = restTemplate.exchange(clientPath + jsonObjectUriPath, HttpMethod.POST,entity, reference);
            posts.add(Objects.requireNonNull(response.getBody()).getData());
        }catch (Exception e){
            log.error("请求{}异常: {}", jsonObjectUriPath, e.getMessage(), e);
        }
        return posts;
    }

}
