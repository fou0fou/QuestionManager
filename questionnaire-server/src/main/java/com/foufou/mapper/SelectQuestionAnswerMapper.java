package com.foufou.mapper;

import com.foufou.entity.SelectAnswer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SelectQuestionAnswerMapper {

    // 根据问卷id删除选择题答案
    @Delete("delete from select_answer where questionnaire_id = #{id}")
    void deleteSelectAnswerByQuestionnaireId(Long id);

    // 根据大选择题id删除选择题答案
    void deleteAnswerByInnerQuestionId(List<Long> selectInnerQuestionIds);

    // 增加答案
    void addAnswer(SelectAnswer selectAnswer);
}
