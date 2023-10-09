package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    private final String  EMAIL_REGEX="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$";
    private final String USERNAME_REGEX = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";

    @Resource
    AuthorizeService service;
//注册
    @PostMapping("/valid-register-email")

    public RestBean<String> validateRegisterEmail(@Pattern(regexp=EMAIL_REGEX) @RequestParam("email")String email, //@pattern为格式校验
                                          HttpSession session){
        String s=service.sendValidateEmail(email,session.getId(),false);
        if(s == null)
            return RestBean.success("邮件已发送，请注意查收");
        else
            return RestBean.failure(400,s);
    }
//  邮件拦截验证
     @PostMapping("/valid-reset-email")
     //重置
    public RestBean<String> validateEmail(@Pattern(regexp=EMAIL_REGEX) @RequestParam("email")String email, //@pattern为格式校验
                                          HttpSession session){
         String s=service.sendValidateEmail(email,session.getId(),true);
         if(s == null)
                 return RestBean.success("邮件已发送，请注意查收");
         else
             return RestBean.failure(400,s);
     }
     //注册实现
    @PostMapping("/register")
    public RestBean<String> registerUser(@Pattern(regexp = USERNAME_REGEX) @Length(min = 2,max = 5) @RequestParam("username") String username, // @Length注解为判断字符长度
                                         @Length(min = 5,max = 6) @RequestParam("password") String password,
                                         @Pattern(regexp=EMAIL_REGEX) @RequestParam("email") String email, // 对应前端四个参数
                                         @Length(min = 6,max = 6) @RequestParam("code") String code,
                                         HttpSession session){
        String s = service.validateAndRegister(username, password, email, code,session.getId());
         if (s == null)
               return RestBean.success("注册成功");
         else
             return RestBean.failure(400,s);

    }
    /**（重置密码）
     * 1.发验证邮件
     * 2.验证验证码是否正确，正确就在Session中存一个标记
     * 3.用户发起重置密码请求，若存在标记，则成功重置
     * **/
    @PostMapping("/start-reset")
    public RestBean<String> startReset( @Pattern(regexp=EMAIL_REGEX) @RequestParam("email") String email,
                                        @Length(min = 6,max = 6) @RequestParam("code") String code,
                                        HttpSession session){
        String s = service.validateOnly(email,code,session.getId());
        if (s == null){
            session.setAttribute("reset-password",email);
            return RestBean.success();
        }else {
            return RestBean.failure(400,s);
        }
    }
    @PostMapping("/do-reset") //重置密码接口，上面为发送邮件接口
    public RestBean<String> resetPassword( @Length(min = 5,max = 6) @RequestParam("password") String password,
                                           HttpSession session){
        String email = (String) session.getAttribute("reset-password");
        if (email == null){
            return RestBean.failure(401,"请先完成邮箱验证");
        }else if (service.resetPassword(password,email)){
            session.removeAttribute("reset-password"); //如果重置成功，则删除原来的信息
            return RestBean.success("密码重置成功");
        }else {
            return RestBean.failure(500,"内部错误");
        }
    }

}
