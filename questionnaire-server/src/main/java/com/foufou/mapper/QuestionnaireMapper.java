package com.foufou.mapper;

import com.foufou.annotation.AutoFill;
import com.foufou.entity.Questionnaire;
import com.foufou.enumeration.OperationType;
import com.foufou.vo.QuestionnaireVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface QuestionnaireMapper {
    //新增问卷
    @AutoFill(OperationType.INSERT)
    void insertPaper(Questionnaire questionnaire);

    // 根据问卷id删除问卷
    @Delete("delete from questionnaire where id = #{id}")
    void deleteById(Long id);

    // 根据问卷id在选择-问卷联立表中查询选择题的id
    @Select("select select_id from questionnaire_select where questionnaire_id = #{id}")
    List<Long> selectByQuestionnaireId01(Long id);

    // 根据问卷id在填空-问卷联立表中查询填空题的id
    @Select("select text_id from questionnaire_text where questionnaire_id = #{id}")
    List<Long> selectByQuestionnaireId02(Long id);



    // 根据问卷id删除问卷-选择联合表数据
    @Delete("delete from questionnaire_select where questionnaire_id = #{id}")
    void deleteQuestionnaireJoinSelectQuestionByQuestionnaireId(Long id);

    // 根据问卷id删除问卷-填空联合表数据
    @Delete("delete from questionnaire_text where questionnaire_id = #{id}")
    void deleteQuestionnaireJoinTextQuestionByQuestionnaireId(Long id);

    // 更新问卷
    @AutoFill(OperationType.UPDATE)
    void updateQuestionnaire(Questionnaire questionnaire);

    // 根据用户id分页查找问卷
    Page<QuestionnaireVO> list(String title, Long userId);

    // 根据id回显问卷信息
    @Select("select * from questionnaire where id = #{id}")
    Questionnaire getPaperById(Long id);

    // 添加次数
    @Update("update questionnaire set fill_count = fill_count+1 where id = #{questionnaireId}")
    void updateFillCount(Long questionnaireId);

    // 根据年级和专业获取问卷
    @Select("select * from questionnaire where grade = #{grade} and major = #{major};")
    Questionnaire getPaperByGradeAndMajor(Integer grade, String major);

    // 更新问卷状态
    @Update("update questionnaire set status = #{status} where id = #{id}")
    void updateStatus(Long id, Integer status);
}
