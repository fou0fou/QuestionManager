package com.foufou.mapper;

import com.foufou.entity.TextAnswer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TextQuestionAnswerMapper {
    // 根据问卷id删除填空题答案
    @Delete("delete from text_answer where questionnaire_id = #{id}")
    void deleteTextAnswerByQuestionnaireId(Long id);

    // 增加答案
    void addTextAnswer(TextAnswer textAnswer);

    // 增加表答案
    void addTableAnswer(TextAnswer textAnswer);

    // 根据大题id删除
    @Delete("delete from text_answer where text_id = #{questionId}")
    void deleteTextAnswerByQuestionId(Long questionId);

    // 根据问题id获取简答题数据
    @Select("select content from text_answer where text_id = #{questionId}")
    List<String> selectAnswerBy(Long questionId);

    // 根据tableId获取平均答案
    @Select("select content from text_answer where questionnaire_id = #{questionnaireId} and table_id = #{tableId}")
    List<String> getAveAnswerFromTableByQuestionnaireIdAndTableId(Long questionnaireId, Long tableId);
}
