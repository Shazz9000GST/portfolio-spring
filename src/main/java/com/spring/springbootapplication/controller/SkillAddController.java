package com.spring.springbootapplication.controller;

import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

@Controller
public class SkillAddController {
  private final SkillService skillService;

  public SkillAddController(SkillService skillService) {
    this.skillService = skillService;
  }

  @GetMapping("/skill-add")
  public String showSkillAdd(
      @RequestParam String category,
      @RequestParam String month,
      HttpSession session,
      Model model) {

    // ログインチェック
    if (session.getAttribute("loginUserId") == null) {
      return "redirect:/login";
    }

    // カテゴリ名と対象月を画面に渡す
    model.addAttribute("category", category);
    model.addAttribute("selectedMonth", month);
    return "skill-add";
  }

  @PostMapping("/skill-add")
  public String addSkill(
      @RequestParam String category,
      @RequestParam String month,
      @RequestParam String skillName,
      @RequestParam Integer studyHours,
      HttpSession session,
      RedirectAttributes redirectAttributes) {

    // ログインチェック
    Object loginUserId = session.getAttribute("loginUserId");

    if (loginUserId == null) {
      return "redirect:/login";
    }

    // バリデーション
    Long categoryId = skillService.findCategoryIdByName(category);

    if (categoryId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "存在しないカテゴリーです");
    }

    // 対象年月をLocalDateに変換
    LocalDate studyMonth;

    try {
      studyMonth = YearMonth.parse(month).atDay(1);
    } catch (DateTimeParseException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "対象年月が不正です");
    }

    // 登録データの作成
    Skill skill = new Skill();

    skill.setUserId(((Number) loginUserId).longValue());
    skill.setCategoryId(categoryId);
    skill.setSkillName(skillName.trim());
    skill.setStudyTime(studyHours);
    skill.setStudyMonth(studyMonth);

    // DBに登録
    skillService.addSkill(skill);

    // 登録成功情報を次の画面に渡す
    redirectAttributes.addFlashAttribute("registrationSuccess", true);
    redirectAttributes.addFlashAttribute("registeredSkillName", skill.getSkillName());
    redirectAttributes.addFlashAttribute("registeredStudyHours", skill.getStudyTime());

    // 同じ画面に戻る
    redirectAttributes.addAttribute("category", category);
    redirectAttributes.addAttribute("month", month);

    return "redirect:/skill-add";
  }
}