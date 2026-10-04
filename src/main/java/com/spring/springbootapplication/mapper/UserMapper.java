package com.spring.springbootapplication.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.spring.springbootapplication.model.User;

@Mapper
public interface UserMapper {

    @Insert("""
        INSERT INTO users (
            user_name,
            email,
            password
        )
        VALUES (
            #{userName},
            #{email},
            #{password}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);


    @Select("""
        SELECT COUNT(*) > 0
        FROM users
        WHERE email = #{email}
        """)
    boolean existsByEmail(@Param("email") String email);
}