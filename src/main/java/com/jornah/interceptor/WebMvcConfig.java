package com.jornah.interceptor;

import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 向MVC中添加自定义组件
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private Tracer tracer;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        registry.addInterceptor(baseInterceptor());
    }

    @Bean
    public BaseInterceptor baseInterceptor(){
        return new BaseInterceptor(tracer);
    }
}
