package com.spring.springbootapplication.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.springbootapplication.form.UserRegisterForm;
import com.spring.springbootapplication.model.User;
import com.spring.springbootapplication.service.UserService;


@Controller
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute(
            "userRegisterForm",
            new UserRegisterForm()
        );

        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("userRegisterForm")
            UserRegisterForm form,
            BindingResult bindingResult,
            HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (userService.existsByEmail(form.getEmail())) {
            bindingResult.rejectValue(
                "email",
                "duplicate",
                "このメールアドレスはすでに登録されています"
            );

            return "register";
        }

        User user = userService.register(form);

        session.setAttribute("loginUserId", user.getId());
        session.setAttribute("loginUserName", user.getUserName());

        return "redirect:/";
    }
}