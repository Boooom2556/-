package com.example.service.impl;

import com.example.entity.auth.Account;
import com.example.mapper.UserMapper;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${spring.mail.username}")
    String from;

    @Resource
    UserMapper mapper;

    @Resource
    MailSender mailSender;

    @Resource
    StringRedisTemplate template;


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    //从数据库中获取
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查找
        if (username == null)
            throw new UsernameNotFoundException("用户名不能为空");
        Account account = mapper.findAccountByNameOrEmail(username);
        if (account == null)
            throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }
    /**
     * 1，先生成对应的验证码，
     * 2，把邮箱和对应的验证码放到Redis里面，过期时间3分钟（此时重新发送邮件，剩余时间低于2分钟，就可以重新发送）
     * 3.发送验证码到指定邮箱
     * 4.如果发送失败，把Redis里面的刚刚插入的删除
     * 5.用户在注册时，再从Redis里面取出对应键值对，然后看验证码是否一致
     */
    @Override
    public String sendValidateEmail(String email,String sessionId,boolean hasAccount) {
        String key = "email:"+ sessionId + ":"+email +":" + hasAccount;//Redis存储信息
        if (Boolean.TRUE.equals(template.hasKey(key))){//时间判断

            Long expire = Optional.ofNullable(template.getExpire(key,TimeUnit.SECONDS)).orElse(0L);   // 获取过期时间
            if (expire > 120)
                return "请求频繁，请稍后再试！";
        }
        //重置密码时需要账户不为空
        Account account = mapper.findAccountByNameOrEmail(email);
        if (hasAccount && account == null)
            return "没有此邮件地址的用户";

        //判断注册的账户是否存在
        if (!hasAccount && account != null)
                  return "此邮箱已被其他用户注册";


        //1.生成六位数验证码，小于1000000
        Random random =new Random();
        int code = random.nextInt(899999)+100000;//直接小于1000000会有非6位生成

        //2.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);   //邮件来自
        message.setTo(email);    //发给谁
        message.setSubject("您的验证邮件");  //邮件标题
        message.setText("验证码是："+code);
        try {//捕获异常,邮件发送问题
              mailSender.send(message);
              template.opsForValue().set(key,String.valueOf(code),3, TimeUnit.MINUTES);
            return null;
        }catch (MailException e){
            e.printStackTrace();
            return "邮件发送失败,请检查地址是否有效";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code,String sessionId) {
        String key = "email:"+ sessionId + ":"+email + ":false"; // 注册时，数据库中不能有账户
        if (Boolean.TRUE.equals(template.hasKey(key))){
          String s = template.opsForValue().get(key);
          if(s == null) return "验证码失效，请重试";
          if (s.equals(code)){
              Account account = mapper.findAccountByNameOrEmail(username);//先找一下是否有用户信息
              if (account != null) return "此用户名已被注册！";
              password = encoder.encode(password);
              template.delete(key);//验证码使用完应该清除
              if (mapper.createAccount(username,password,email) > 0) {
                  return null;
              }else {
                  return "内部错误";
              }
          }else {
              return "验证码错误，请检查后再填写";
          }
        } else {
            return "请先发送电子邮件验证码";
        }
    }

    @Override
    public String validateOnly(String email, String code, String sessionId) {
        String key = "email:"+ sessionId + ":"+email + ":true";  // 重置时，应该存在用户
        if (Boolean.TRUE.equals(template.hasKey(key))){
            String s = template.opsForValue().get(key);
            if(s == null) return "验证码失效，请重试";
            if (s.equals(code)){
                template.delete(key);
                return null;
            }else {
                return "验证码错误，请检查后再填写";
            }
        } else {
            return "请先发送电子邮件验证码!!";
        }
    }

    @Override
    public boolean resetPassword(String password, String email) {
        password = encoder.encode(password); //新密码要加密
        return mapper.resetPasswordByEmail(password,email) > 0;
    }
}
