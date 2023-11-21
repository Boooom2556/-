package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//解决axios跨域
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                //是否发送cookie
                .allowCredentials(true)
                //哪些原始放行域
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}
