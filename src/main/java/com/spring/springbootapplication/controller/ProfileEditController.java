package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.model.User;
import com.spring.springbootapplication.service.UserService;
import com.spring.springbootapplication.form.ProfileEditForm;
import com.spring.springbootapplication.service.ProfileImageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.IOException;

@Controller
public class ProfileEditController {
  private final com.spring.springbootapplication.service.UserService userService;
  private final ProfileImageService profileImageService;

  public ProfileEditController(UserService userService, ProfileImageService profileImageService) {
    this.userService = userService;
    this.profileImageService = profileImageService;
  }

  @GetMapping("/profile-edit")
  public String showEditForm(HttpSession session, Model model) {
    Long loginUserId = (Long) session.getAttribute("loginUserId");

    if (loginUserId == null) {
      return "redirect:/login";
    }

    User loginUser = userService.findById(loginUserId);

    if (loginUser == null) {
      session.invalidate();
      return "redirect:/login";
    }

    model.addAttribute("loginUser", loginUser);

    ProfileEditForm form = new ProfileEditForm();
    form.setUserBio(loginUser.getUserBio());
    model.addAttribute("profileEditForm", form);
    
    return "profile-edit";
  }

  @PostMapping("/profile-edit")
  public String updateProfile(
    @Valid @ModelAttribute("profileEditForm") ProfileEditForm form,
    BindingResult bindingResult,
    HttpSession session,
    Model model) throws IOException {
    
  Long loginUserId = (Long) session.getAttribute("loginUserId");

  if (loginUserId == null) {
    return "redirect:/login";
  }

  User loginUser = userService.findById(loginUserId);

  if (loginUser == null) {
    session.invalidate();
    return "redirect:/login";
  }

  // バリデーションエラー
  if (bindingResult.hasErrors()) {
    model.addAttribute("loginUser", loginUser);
    return "profile-edit";
  }


  // 選択されたアバター画像を保存
  String profileImagePath = profileImageService.saveImage(form.getProfileImageFile());

  // 自己紹介文を更新
  userService.updateProfile(loginUserId, form.getUserBio(), profileImagePath);

  return "redirect:/";
  }
}
