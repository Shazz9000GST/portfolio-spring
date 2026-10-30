package com.spring.springbootapplication.mapper;

import com.spring.springbootapplication.model.Skill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

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

  @Select("""
        SELECT
            id,
            user_id AS userId,
            category_id AS categoryId,
            skill_name AS skillName,
            study_time AS studyTime,
            study_month AS studyMonth
        FROM learning_data
        WHERE user_id = #{userId}
        AND study_month = #{studyMonth}
        ORDER BY category_id, id
        """)
    List<Skill> findSkillsByUserIdAndMonth(
            @Param("userId") Long userId,
            @Param("studyMonth") LocalDate studyMonth
    );

    @Select("""
        SELECT COUNT(*) > 0
        FROM learning_data
        WHERE user_id = #{userId}
        AND category_id = #{categoryId}
        AND study_month = #{studyMonth}
        AND skill_name = #{skillName}
    """)

    boolean existsByUserIdAndCategoryIdAndMonthAndSkillName(
        @Param("userId") Long userId,
        @Param("categoryId") Long categoryId,
        @Param("studyMonth") LocalDate studyMonth,
        @Param("skillName") String skillName);
}
