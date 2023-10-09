package com.example.entity.user;

import lombok.Data;

@Data
//用户信息，与数据库无关
public class AccountUser {
    int id;
    String username;
    String email;
}
