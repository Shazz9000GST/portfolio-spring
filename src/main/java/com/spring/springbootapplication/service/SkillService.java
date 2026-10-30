package com.spring.springbootapplication.service;

import com.spring.springbootapplication.mapper.SkillMapper;
import com.spring.springbootapplication.model.Skill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

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
}
