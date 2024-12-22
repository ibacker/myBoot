package com.ibacker.myboot.config;

import com.ibacker.myboot.interfafce.Interceptor.restTemplateInterceptor.TrackLogClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Value("${resttemplate.connection.timeout}")
    private int restTemplateConnectionTimeout;

    @Value("${resttemplate.read.timeout}")
    private int restTemplateReadTimeout;


    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory simpleClientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        // add Interceptor
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList() {{
            add(new TrackLogClientHttpRequestInterceptor());
        }};
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(restTemplateConnectionTimeout);
        requestFactory.setReadTimeout(restTemplateReadTimeout);
        return requestFactory;
    }
}
