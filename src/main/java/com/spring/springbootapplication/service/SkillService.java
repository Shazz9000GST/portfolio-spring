package com.spring.springbootapplication.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
