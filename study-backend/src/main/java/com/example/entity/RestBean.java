package com.example.entity;

import lombok.Data;

@Data
public class RestBean <T>{
    private int status;//当前状态
    private boolean success; //是否成功
    private T message;


    //构造函数
    private RestBean(int status, boolean success, T message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    //工具类
    public static <T> RestBean<T> success(){
        return new RestBean<>(200,true,null);// 登录成功返回的结果
    }

    //多个工具类
    public static <T> RestBean<T> success(T data){
        return new RestBean<>(200,true,data);
    }

    public static <T> RestBean<T> failure(int status){
        return new RestBean<>(status,false,null);//登录失败的结果
    }

    public static <T> RestBean<T> failure(int status,T data){
        return new RestBean<>(status,false,data);
    }

}
