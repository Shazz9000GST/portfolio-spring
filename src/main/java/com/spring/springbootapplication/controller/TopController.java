package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.model.User;
import com.spring.springbootapplication.service.UserService;
import com.spring.springbootapplication.service.SkillService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {
        private final UserService userService;
        private final SkillService skillService;

        public TopController(UserService userService, SkillService skillService) {
                this.userService = userService;
                this.skillService = skillService;
        }

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

        User loginUser = userService.findById(loginUserId);

        var chartData = skillService.getMonthlyStudyHours(loginUserId);

        model.addAttribute("loginUserName", loginUserName);
        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("chartData", chartData);

        return "top";
    }
}