package com.spring.springbootapplication.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.spring.springbootapplication.form.UserRegisterForm;
import com.spring.springbootapplication.form.LoginForm;
import com.spring.springbootapplication.mapper.UserMapper;
import com.spring.springbootapplication.model.User;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserRegisterForm form) {

        User user = new User();

        user.setUserName(form.getUserName());
        user.setEmail(form.getEmail());

        String hashedPassword =
                passwordEncoder.encode(form.getPassword());
        user.setPassword(hashedPassword);

        userMapper.insert(user);

        return user;
    }

    public boolean existsByEmail(String email) {
        return userMapper.existsByEmail(email);
    }

    public User login(LoginForm form) {
        User user = userMapper.findByEmail(form.getEmail());

        if (user == null) {
            return null;
        }

        boolean passwordMatched =
            passwordEncoder.matches(
                form.getPassword(),
                user.getPassword()
            );

        if (!passwordMatched) {
            return null;
        }

        return user;
    }
}