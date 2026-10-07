package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.form.LoginForm;
import com.spring.springbootapplication.model.User;
import com.spring.springbootapplication.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
  private final UserService userService;

  public LoginController(UserService userService) {
    this.userService = userService;
  }

  // ログイン画面表示
  @GetMapping("/login")
  public String showLoginForm(Model model) {
    model.addAttribute("loginForm", new LoginForm());

    return "login";
  }

  // ログイン処理
  @PostMapping("/login")
  public String login(
      @Valid @ModelAttribute("loginForm") LoginForm form,
      BindingResult bindingResult,
      Model model,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
        // 入力値のバリデーション
        if (bindingResult.hasErrors()) {
          return "login";
        }

        // ログイン認証
        User user = userService.login(form);

        // 認証失敗
        if (user == null) {
          redirectAttributes.addFlashAttribute(
            "loginError",
            "メールアドレス、もしくはパスワードが間違っています"
          );

          return "redirect:/login";
        }

        // 認証成功
        session.setAttribute("loginUserId", user.getId());
        session.setAttribute("loginUserName", user.getUserName());

        return "redirect:/";
      }
}
