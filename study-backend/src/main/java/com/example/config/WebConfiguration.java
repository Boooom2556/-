package com.example.config;

import com.example.interceptor.AuthorizeInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration     //不做这个了
public class WebConfiguration implements WebMvcConfigurer {

    @Resource
    AuthorizeInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(interceptor)
                .addPathPatterns("/**") //拦截所有地址
                .excludePathPatterns("/api/auth/**")//排除发送邮件
                .excludePathPatterns("/data/**"); //排除发送邮件
    }
}
