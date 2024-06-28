package com.example.config;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.RestBean;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;//添加检验功能

    //JDBC存储remember me的token
    @Resource
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           PersistentTokenRepository repository) throws Exception {
        return http
                .authorizeHttpRequests()//权限校验
                .requestMatchers("/api/auth/**").permitAll()//全部放行
                .requestMatchers("/data/").permitAll()

                .anyRequest().authenticated()//验证全部的请求
                .and()
                .formLogin()//表单登录（前端）
                .loginProcessingUrl("/api/auth/login") //登录接口
                .successHandler(this::onAuthenticationSuccess)//前端返回数据,后面定义方法
                .failureHandler(this::onAuthenticationFailure)
                .and()
                .logout()//退出登录
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(this::onAuthenticationSuccess)
                .and()
                .rememberMe()//cookie
                .rememberMeParameter("remember") // 将参数改为自己定义的
                .tokenRepository(repository)
                .tokenValiditySeconds(3600 * 24 * 3) //记住3天

                .and()
                //.userDetailsService(authorizeService) //添加校验
                .csrf()
                .disable()
                .cors()
                .configurationSource(this.corsConfigurationSource())
                .and()
                .exceptionHandling()//权限响应
                .authenticationEntryPoint(this::onAuthenticationFailure)//没有权限时的响应
                .and()
                .build();//配置结束
    }
    //JDBC存储rememberme的token


    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository= new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false); // 第一次运行时创建表，后面改为false
        return jdbcTokenRepository;
    }


    //跨域配置
    private CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern("*");//允许所有的跨域请求
        cors.setAllowCredentials(true);//允许携带cookie
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",cors);//所有的都走这里
        return source;
    }


    //添加校验,运行不再生成code，而是使用自己的code
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception{
        return security.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authorizeService)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        response.setCharacterEncoding("utf-8"); //返回格式
        if (request.getRequestURI().endsWith("/login"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功"))); //返回内容
        else if (request.getRequestURI().endsWith("/logout"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("退出成功")));
        if (request.getRequestURI().endsWith("/data/list"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success(dataSource)));

    }
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8"); //返回格式
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401,exception.getMessage()))); //返回内容
    }

}
