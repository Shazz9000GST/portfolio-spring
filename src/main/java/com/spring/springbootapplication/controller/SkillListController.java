package com.spring.springbootapplication.controller;

import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.spring.springbootapplication.service.SkillService;
import com.spring.springbootapplication.model.Skill;

@Controller
public class SkillListController {
    private final SkillService skillService;

    public SkillListController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/skill-list")
    public String showSkillList(
            @RequestParam(required = false) String month,
            HttpSession session,
            Model model) {
        // ログインチェック
        Long loginUserId = (Long) session.getAttribute("loginUserId");

        if (loginUserId == null) {
            return "redirect:/login";
        }

        // 当月を取得
        YearMonth currentMonth = YearMonth.now(ZoneId.of("Asia/Tokyo"));

        // 当月・前月・前々月の選択肢を作成
        List<MonthOption> monthOptions = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M月");

        for (int i = 0; i < 3; i++) {
            YearMonth target = currentMonth.minusMonths(i);

            monthOptions.add(
                    new MonthOption(
                            target.toString(),
                            target.format(formatter)));
        }

        // 初期表示は当月
        YearMonth selectedMonth = currentMonth;

        // 指定された月が選択可能な月なら切り替える
        if (month != null) {
            for (MonthOption option : monthOptions) {
                if (option.value().equals(month)) {
                    selectedMonth = YearMonth.parse(month);
                    break;
                }
            }
        }

        // 選択月の学習項目をDBから取得
        List<Skill> skills = skillService.getSkillsByMonth(loginUserId, selectedMonth);

        // カテゴリ別に整理
        List<SkillCategory> categories = new ArrayList<>();

        List<String> categoryNames = List.of(
                "バックエンド",
                "フロントエンド",
                "インフラ");

        for (String categoryName : categoryNames) {
            // カテゴリ名からIDを取得
            Long categoryId = skillService.findCategoryIdByName(categoryName);

            // 対象カテゴリの学習項目を抽出
            List<SkillItem> items = skills.stream()
                    .filter(skill -> categoryId != null &&
                            categoryId.equals(skill.getCategoryId()))
                    .map(skill -> new SkillItem(
                            skill.getSkillName(),
                            skill.getStudyTime()))
                    .toList();

            // カテゴリと学習項目をまとめる
            categories.add(new SkillCategory(categoryName, items));
        }

        model.addAttribute("monthOptions", monthOptions);
        model.addAttribute("selectedMonth", selectedMonth.toString());
        model.addAttribute("categories", categories);

        return "skill-list";
    }

    // 月選択用
    public record MonthOption(String value, String label) {
    }

    // 個々のスキル
    public record SkillItem(String name, int studyHours) {
    }

    // カテゴリー
    public record SkillCategory(
            String name,
            List<SkillItem> items) {
    }
}