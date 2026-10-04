package com.spring.springbootapplication.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {

    @GetMapping("/")
    public String top(
            HttpSession session,
            Model model) {

        Long loginUserId =
                (Long) session.getAttribute("loginUserId");
        String loginUserName =
                (String) session.getAttribute("loginUserName");
        boolean loggedIn = loginUserId != null;

        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("loginUserName", loginUserName);

        return "top";
    }
}