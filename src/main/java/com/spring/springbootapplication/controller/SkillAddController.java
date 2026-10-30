package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.service.SkillService;
import com.spring.springbootapplication.model.Skill;
import com.spring.springbootapplication.mapper.SkillMapper;
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
      @RequestParam(required = false) String studyHours,
      HttpSession session,
      RedirectAttributes redirectAttributes) {

    // ログインチェック
    Object loginUserId = session.getAttribute("loginUserId");

    if (loginUserId == null) {
      return "redirect:/login";
    }

    // バリデーション
    boolean hasError = false;

    // 項目名のバリデーション
    if (skillName == null || skillName.isBlank()) {
      // 未入力チェック
      redirectAttributes.addFlashAttribute(
        "skillNameError",
        "項目名は必ず入力してください");
      hasError = true;

    } else if (skillName.trim().length() > 50) {
      // 文字数チェック
      redirectAttributes.addFlashAttribute(
        "skillNameError",
        "項目名は50文字以内で入力してください");
      hasError = true;
    }

    // 学習時間のバリデーション
    Integer parsedStudyHours = null;

    if (studyHours == null || studyHours.isBlank()) {
      // 未入力チェック
      redirectAttributes.addFlashAttribute(
        "studyHoursError",
        "学習時間は必ず入力してください");
      hasError = true;
    } else {
      try {
        // 文字列を整数に変換
        parsedStudyHours = Integer.parseInt(studyHours.trim());

        // 0未満のチェック
        if (parsedStudyHours < 0) {
          redirectAttributes.addFlashAttribute(
            "studyHoursError",
            "学習時間は0以上の数字で入力してください");
          hasError = true;
        }
      } catch (NumberFormatException e) {
        // 整数に変換できなかった場合
        redirectAttributes.addFlashAttribute(
          "studyHoursError",
          "学習時間は0以上の数字で入力してください");
        hasError = true;
      }
    }

    // エラーがある場合は登録せずに入力画面へ戻す
    if (hasError) {
      redirectAttributes.addFlashAttribute("inputSkillName", skillName);
      redirectAttributes.addFlashAttribute("inputStudyHours", studyHours);
      redirectAttributes.addAttribute("category", category);
      redirectAttributes.addAttribute("month", month);

      return "redirect:/skill-add";
    }

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

    // 項目名の重複チェック
    String trimmedSkillName = skillName.trim();

    boolean isDuplicate = skillService.existsSkillName(
      ((Number) loginUserId).longValue(),
      categoryId,
      studyMonth,
      trimmedSkillName);

    // 重複している場合
    if (isDuplicate) {
      // エラーメッセージ
      redirectAttributes.addFlashAttribute(
        "skillNameError",
        trimmedSkillName + "は既に登録されています");

      // 入力値を保持
      redirectAttributes.addFlashAttribute("inputSkillName", skillName);
      redirectAttributes.addFlashAttribute("inputStudyHours", studyHours);

      // カテゴリと年月を引き継ぐ
      redirectAttributes.addAttribute("category", category);
      redirectAttributes.addAttribute("month", month);

      // 登録せずに入力画面へ戻る
      return "redirect:/skill-add";
    }

    // 登録データの作成
    Skill skill = new Skill();

    skill.setUserId(((Number) loginUserId).longValue());
    skill.setCategoryId(categoryId);
    skill.setSkillName(skillName.trim());
    skill.setStudyTime(parsedStudyHours);
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