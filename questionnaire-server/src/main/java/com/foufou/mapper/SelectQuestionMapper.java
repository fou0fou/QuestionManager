package com.foufou.mapper;

import com.foufou.entity.Option;
import com.foufou.entity.SelectInnerQuestion;
import com.foufou.entity.SelectQuestion;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SelectQuestionMapper {
    // 根据大选择题id查找内部嵌套选择题id
    List<Long> selectInnerQuestionIdBySelectQuestionIds(List<Long> selectQuestionIds);

    // 根据id删除内部选择题
    void deleteSelectInnerQuestionById(List<Long> selectInnerQuestionIds);

    // 根据id删除选择题
    void deleteSelectQuestionById(List<Long> selectQuestionIds);

    // 根据id删除选项信息
    void deleteOptionById(List<Long> selectQuestionIds);

    // 根据id查询大选择题信息
    @Select("select * from select_question where id = #{selectQuestionId}")
    SelectQuestion selectQuestionById(Long selectQuestionId);

    // 根据大选择题id查询内部嵌套选择题信息
    @Select("select * from select_inner_question where question_id = #{selectQuestionId}")
    List<SelectInnerQuestion> selectInnerQuestionBySelectQuestionId(Long selectQuestionId);

    // 根据问题id查询选项数据
    @Select("select * from `option` where question_id = #{selectQuestionId}")
    List<Option> selectOptionsByQuestionId(Long selectQuestionId);

    // 创建问卷
    void insertQuestion(SelectQuestion selectQuestion);

    // 在问卷-选择关联表中插入数据
    @Insert("insert into questionnaire_select(questionnaire_id, select_id, seq_num) VALUE (#{questionnaireId},#{questionId},#{seqNum})")
    void insertJointData(Long questionnaireId, Long questionId, Integer seqNum);

    // 更新大选择题的信息
    void updateSelectQuestion(SelectQuestion selectQuestion);

    // 根据id获取内部嵌套问题
    @Select("select * from select_inner_question where id = #{id}")
    SelectInnerQuestion selectInnerQuestionById(Long id);

    // 插入内部嵌套选择题
    void insertSelectInnerQuestion(SelectInnerQuestion selectInnerQuestion);

    // 更新内部嵌套问题
    void updateSelectInnerQuestion(SelectInnerQuestion selectInnerQuestion);

    // 根据问题id和sequence获取选项信息
    @Select("select * from `option` where question_id = #{questionId} and sequence = #{sequence}")
    Option selectOptionsBySequence(Long questionId, Integer sequence);

    // 插入选项
    void insertOption(Option option);

    // 更新选项
    void updateOption(Option option);

    // 根据选择题id删除问卷-选择关联表的数据
    @Delete("delete from questionnaire_select where select_id = #{id}")
    void deleteJointTableData(Long id);

    // 删除问题后将seqNum-1
    @Update("update questionnaire_select set seq_num = seq_num-1 where questionnaire_id = #{questionnaireId} and seq_num > #{seqNum}")
    void minusQuestionSeq(Long questionnaireId, Integer seqNum);

    // 根据问卷id和问题id在问卷-选择联立表中查找对应的seqNum
    @Select("select seq_num from questionnaire_select where questionnaire_id = #{questionnaireId} and select_id = #{questionId}")
    Integer getSeqNumByQuestionnaireIdAndQuestionId(Long questionnaireId, Long questionId);

    // 新增问题后将seqNum+1
    @Update("update questionnaire_select set seq_num = seq_num+1 where questionnaire_id = #{questionnaireId} and seq_num >= #{seqNum}")
    void addQuestionSeq(Long questionnaireId, Integer seqNum);

    // 根据已有的sequence删除多余的
    void deleteExtraOption(Long id, List<Integer> alreadyExistOptionSequences);

    // 根据已有的嵌套id删除多余的
    void deleteExtraInnerQuestions(Long questionId, List<Long> selectInnerQuestionIds);
}
