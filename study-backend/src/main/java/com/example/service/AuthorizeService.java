package com.example.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {
    //发送邮件
    String sendValidateEmail(String email,String sessionId,boolean hasAccount);  //bool判断要重置密码的邮箱是否已经存在
    String validateAndRegister(String username,String password,String email,String code,String sessionId);  //验证并注册一个实体类
    String validateOnly(String email,String code,String sessionId);
    boolean resetPassword(String password,String email);
}
