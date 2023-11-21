package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.auth.Account;
import com.example.entity.auth.data;
import com.example.entity.user.AccountUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);  //

    @Select("select * from user where username = #{text} or email = #{text}")
    AccountUser findAccountUserByNameOrEmail(String text);

    @Insert("insert into user (email,username,password) values(#{email}, #{username}, #{password})")
    int createAccount(@Param("username") String username,@Param("password") String password,@Param("email") String email);

    @Update("update user set password = #{password} where email = #{email}")
    int resetPasswordByEmail(String password,String email);
}
