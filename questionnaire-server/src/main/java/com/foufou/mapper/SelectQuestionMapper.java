package com.foufou.mapper;

import com.foufou.dto.OptionDTO;
import com.foufou.dto.SelectInnerQuestionDTO;
import com.foufou.entity.Option;
import com.foufou.entity.SelectInnerQuestion;
import com.foufou.entity.SelectQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    void deleteOperationById(List<Long> selectQuestionIds);

    // 根据id查询大选择题信息
    @Select("select * from select_question where id = #{selectQuestionId}")
    SelectQuestion selectQuestionById(Long selectQuestionId);

    // 根据大选择题id查询内部嵌套选择题信息
    @Select("select * from select_inner_question where question_id = #{selectQuestionId}")
    List<SelectInnerQuestion> selectInnerQuestionBySelectQuestionId(Long selectQuestionId);

    // 根据问题id查询选项数据
    @Select("select * from `option` where question_id = #{selectQuestionId}")
    List<Option> selectOptionsByQuestionId(Long selectQuestionId);
}
