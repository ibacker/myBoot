package com.ibacker.myboot.interfafce.controller;

import com.ibacker.myboot.domain.resttemplate.entity.Post;
import com.ibacker.myboot.infrastructure.bean.ResultObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RequestMapping("services/resttemplate")
public class RestTemplateController {

    private final String domain = "http://jsonplaceholder.typicode.com";
    @Resource
    RestTemplate restTemplate;

    @GetMapping("getpost/{id}")
    public ResultObject getForObject(@PathVariable String id) {
        ResultObject resultObject = new ResultObject();
        String url = domain + "/posts/{id}";
        Post post = restTemplate.getForObject(url, Post.class, id);
        resultObject.setResult(post);
        return resultObject;
    }

    @GetMapping("getposten")
    public ResultObject getForEntity(@RequestParam String id) {
        ResultObject resultObject = new ResultObject();
        String url = domain + "/posts/{id}";
        ResponseEntity<Post> response = restTemplate.getForEntity(url, Post.class, id);
        resultObject.setResult(response.getBody());
        return resultObject;
    }

    @GetMapping("getcomments")
    public ResultObject getCommentsForEntity(@RequestParam String postId) {
        ResultObject resultObject = new ResultObject();
        String url = domain + "/posts/{postId}/comments";
        ResponseEntity<Post[]> response = restTemplate.getForEntity(url, Post[].class, postId);
        resultObject.setResult(response.getBody());
        return resultObject;
    }

    //
    @PostMapping("postforObject")
    public ResultObject postForObject(@RequestBody Post post) {
        ResultObject resultObject = new ResultObject();
        String url = domain + "/posts";
        // raw use of postForObject
//        Object object = restTemplate.postForObject(url,post,Post.class);

        // raw use of HttpEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>() {{
            add("IBA", "IBA");
            add("IBC", "IBC");
        }};
        headers.addAll(map);
        HttpEntity<Post> entity = new HttpEntity<>(post, headers);
        Object object = restTemplate.postForObject(url, entity, Post.class);
        resultObject.setResult(object);
        return resultObject;
    }


}
