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

    @Select("""
            SELECT
                id,
                user_name AS userName,
                email,
                password,
                profile_image AS profileImage,
                user_bio AS userBio,
                created_date AS createdDate,
                updated_date AS updatedDate
            FROM users
            WHERE email = #{email}
            LIMIT 1
            """)
    User findByEmail(@Param("email") String email);
}