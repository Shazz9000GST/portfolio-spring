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

@Controller
public class SkillListController {
    @GetMapping("/skill-list")
    public String showSkillList(
            @RequestParam(required = false) String month,
            HttpSession session,
            Model model
    ) {
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
                    target.format(formatter)
                )
            );
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

        // 選択月の仮データ
        List<SkillCategory> categories = getMockSkills(selectedMonth, currentMonth);

        model.addAttribute("monthOptions", monthOptions);
        model.addAttribute("selectedMonth", selectedMonth.toString());
        model.addAttribute("categories", categories);

        return "skill-list";
    }

    // 月別の仮データ
    private List<SkillCategory> getMockSkills(
            YearMonth selectedMonth,
            YearMonth currentMonth
    ) {
        // 当月
        if (selectedMonth.equals(currentMonth)) {
            return List.of(
                new SkillCategory("バックエンド", List.of(
                    new SkillItem("Java", 40),
                    new SkillItem("Spring Boot", 30)
                )),
                new SkillCategory("フロントエンド", List.of(
                    new SkillItem("HTML", 20),
                    new SkillItem("CSS", 15)
                )),
                new SkillCategory("インフラ", List.of(
                    new SkillItem("Docker", 10)
                ))
            );
        }

        // 前月
        if (selectedMonth.equals(currentMonth.minusMonths(1))) {
            return List.of(
                new SkillCategory("バックエンド", List.of(
                    new SkillItem("Ruby", 50),
                    new SkillItem("Rails", 30)
                )),
                new SkillCategory("フロントエンド", List.of(
                    new SkillItem("JavaScript", 20)
                )),
                new SkillCategory("インフラ", List.of())
            );
        }

        // 前々月
        return List.of(
            new SkillCategory("バックエンド", List.of(
                new SkillItem("SQL", 25)
            )),
            new SkillCategory("フロントエンド", List.of(
                new SkillItem("HTML", 15)
            )),
            new SkillCategory("インフラ", List.of(
                new SkillItem("Linux", 10)
            ))
        );
    }

    // 月選択用
    public record MonthOption(String value, String label) {}

    // 個々のスキル
    public record SkillItem(String name, int studyHours) {}

    // カテゴリー
    public record SkillCategory(
            String name,
            List<SkillItem> items
    ) {}
}