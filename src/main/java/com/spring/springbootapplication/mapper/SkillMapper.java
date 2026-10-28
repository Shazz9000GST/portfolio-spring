package com.spring.springbootapplication.mapper;

import org.apache.ibatis.annotation.Insert;
import org.apache.ibatis.annotation.Mapper;
import org.apache.ibatis.annotation.Options;
import org.apache.ibatis.annotation.Select;

@Mapper
public interface SkillMapper {
  @Insert("""
      INSERT INTO learning_data (
          user_id,
          category_id,
          skill_name,
          study_time,
          study_month
      )
      VALUES (
          #{userId},
          #{categoryId},
          #{skillName},
          #{studyTime},
          #{studyMonth}
      )
      """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertSkill(Skill skill);

  @Select("""
      SELECT id
      FROM categories
      WHERE name = #{categoryName}
      """)
  Long findCategoryIdByName(String categoryName);
}
