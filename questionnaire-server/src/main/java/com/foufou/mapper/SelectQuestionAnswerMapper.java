package com.foufou.mapper;

import com.foufou.entity.SelectAnswer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    // 根据问卷id和问题id获取答案
    @Select("select * from select_answer where questionnaire_id = #{id} and select_id = #{questionId}")
    List<SelectAnswer> getAnswerByQuestionnaireIdAndQuestionId(Long id, Long questionId);

    // 根据问卷id和问题id和innerid获取答案
    @Select("select * from select_answer where questionnaire_id = #{id} and select_id = #{questionId} and select_inner_id = #{selectInnerId}")
    List<SelectAnswer> getAnswerByQuestionnaireIdAndQuestionIdAndInnerId(Long id, Long questionId, Long selectInnerId);

}
