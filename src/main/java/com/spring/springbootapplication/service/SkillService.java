package com.spring.springbootapplication.service;

import com.spring.springbootapplication.mapper.SkillMapper;
import com.spring.springbootapplication.model.Skill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.ArrayList;
import java.time.ZoneId;

@Service
public class SkillService {
  private final SkillMapper skillMapper;

  public SkillService(SkillMapper skillMapper) {
    this.skillMapper = skillMapper;
  }

  // 学習項目登録
  @Transactional
  public void addSkill(Skill skill) {
    skillMapper.insertSkill(skill);
  }

  public Long findCategoryIdByName(String categoryName) {
      return skillMapper.findCategoryIdByName(categoryName);
  }

  // 年月と学習項目を取得
  public List<Skill> getSkillsByMonth(Long userId, YearMonth month) {
    LocalDate studyMonth = month.atDay(1);
    return skillMapper.findSkillsByUserIdAndMonth(userId, studyMonth);
  }

  // 同じユーザー、月、カテゴリ内に同じ項目名が存在するか確認
  public boolean existsSkillName(
    Long userId,
    Long categoryId,
    LocalDate studyMonth,
    String skillName) {

    return skillMapper.existsByUserIdAndCategoryIdAndMonthAndSkillName(
      userId,
      categoryId,
      studyMonth,
      skillName);
    }

  // 学習時間を更新する
  public int updateStudyTime(Long skillId, Long userId, Integer studyHours) {
    return skillMapper.updateStudyTime(skillId, userId, studyHours);
  }

  // 項目削除
  @Transactional
  public int deleteSkill(Long skillId, Long userId) {
    return skillMapper.deleteSkill(skillId, userId);
  }

  // チャート用の学習時間を取得
  public List<MonthlyStudyHours> getMonthlyStudyHours(Long userId) {
    YearMonth currentMonth = YearMonth.now(ZoneId.of("Asia/Tokyo"));

    Long backendId = findCategoryIdByName("バックエンド");
    Long frontendId = findCategoryIdByName("フロントエンド");
    Long infraId = findCategoryIdByName("インフラ");

    List<MonthlyStudyHours> chartData = new ArrayList<>();

    for (int i = 2; i >= 0; i--) {
      YearMonth targetMonth = currentMonth.minusMonths(i);

      List<Skill> skills = getSkillsByMonth(userId, targetMonth);

      int backendHours = 0;
      int frontendHours = 0;
      int infraHours = 0;

      for (Skill skill : skills) {
        if (backendId != null && backendId.equals(skill.getCategoryId())) {
          backendHours += skill.getStudyTime();
        } else if (frontendId != null && frontendId.equals(skill.getCategoryId())) {
          frontendHours += skill.getStudyTime();
        } else if (infraId != null && infraId.equals(skill.getCategoryId())) {
          infraHours += skill.getStudyTime();
        }
      }

      String monthLabel = switch (i) {
        case 2 -> "先々月";
        case 1 -> "先月";
        default -> "今月";
      };

      chartData.add(new MonthlyStudyHours(monthLabel, backendHours, frontendHours, infraHours));
    }
    return chartData;
  }
  public record MonthlyStudyHours(String month, int backend, int frontend, int infra) {

  }
}
