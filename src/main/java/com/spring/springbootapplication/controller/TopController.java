package com.spring.springbootapplication.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {

    @GetMapping("/")
    public String top(HttpSession session, Model model) {
        Long loginUserId =
                (Long) session.getAttribute("loginUserId");

        boolean loggedIn = loginUserId != null;
        
        if (!loggedIn) {
                return "redirect:/login";
        }

        String loginUserName =
                (String) session.getAttribute("loginUserName");

        model.addAttribute("loginUserName", loginUserName);
        model.addAttribute("loggedIn", loggedIn);

        return "top";
    }
}