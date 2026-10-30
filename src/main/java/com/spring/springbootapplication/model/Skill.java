package com.spring.springbootapplication.model;

import java.time.LocalDate;

public class Skill {
  private Long id;
  private Long userId;
  private Long categoryId;
  private String skillName;
  private Integer studyTime;
  private LocalDate studyMonth;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id =id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getSkillName() {
    return skillName;
  }

  public void setSkillName(String skillName) {
    this.skillName = skillName;
  }

  public Integer getStudyTime() {
    return studyTime;
  }

  public void setStudyTime(Integer studyTime) {
    this.studyTime =studyTime;
  }

  public LocalDate getStudyMonth() {
    return studyMonth;
  }

  public void setStudyMonth(LocalDate studyMonth) {
    this.studyMonth = studyMonth;
  }
}
