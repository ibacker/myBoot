package com.ibacker.myboot.config;

import com.ibacker.myboot.interfafce.Interceptor.AuthorityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器配置
 * 测试拦截器执行顺序
 */
@Configuration
public class InterceptorAdapterConfig implements WebMvcConfigurer {
    @Resource
    private AuthorityInterceptor authorityInterceptor;

    @Resource
    private com.ibacker.myboot.interfafce.Interceptor.AInterceptor AInterceptor;

    @Resource
    private com.ibacker.myboot.interfafce.Interceptor.BInterceptor BInterceptor;


    /**
     * Interceptor 拦截器
     * 执行顺序; PRE正向顺序; POST\AFTER 反向逆序
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorityInterceptor).addPathPatterns("/*/interceptor/*").order(200);
        registry.addInterceptor(AInterceptor).addPathPatterns("/*/interceptor/*").order(100);
        registry.addInterceptor(BInterceptor).addPathPatterns("/**").order(300);
    }
}