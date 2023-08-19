package com.ibacker.myboot.config;

import com.ibacker.myboot.interfafce.filter.RequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class FilterConfig {
    @Resource
    RequestFilter requestFilter;

    @Bean
    public FilterRegistrationBean<RequestFilter> testFilter3RegistrationBean() {
        FilterRegistrationBean<RequestFilter> registration = new FilterRegistrationBean<>(requestFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(1); // 值越小越靠前，此处配置有效
        return registration;
    }
}
