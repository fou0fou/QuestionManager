package com.foufou.mapper;

import com.foufou.dto.TableContentDTO;
import com.foufou.entity.TableContent;
import com.foufou.entity.TextQuestion;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TextQuestionMapper {
    // 根据id删除填空题
    void deleteTextQuestionById(List<Long> textQuestionIds);

    // 根据id获取填空题信息
    List<TextQuestion> selectByQuestionIds(List<Long> textQuestionIds);

    // 新增填空题，并返回主键
    void insertQuestion(TextQuestion textQuestion);

    // 在问卷-填空关联表中插入数据
    @Insert("insert into questionnaire_text(questionnaire_id, text_id, seq_num) VALUE (#{questionnaireId}, #{questionId}, #{seqNum})")
    void insertJointData(Long questionnaireId, Long questionId, Integer seqNum);

    // 更新问卷
    void updateQuestion(TextQuestion textQuestion);

    // 根据填空题id在table_content表中查找关联数据
    @Select("select * from table_content where question_id = #{id}")
    List<TableContentDTO> selectTableDataByQuestionId(Long id);

    // 根据填空题id删除表数据
    void deleteTableDataByQuestionId(List<Long> textQuestionIds);

    // 插入表数据
    void insertTableContent(TableContent tableContent);

    // 根据问卷id和问题id在问卷-填空联立表中查找对应的seqNum
    @Select("select seq_num from questionnaire_text where questionnaire_id = #{questionnaireId} and text_id = #{questionId}")
    Integer getSeqNumByQuestionnaireIdAndQuestionId(Long questionnaireId, Long questionId);

    // 根据问卷id和问题id删除问卷-填空联立表中的数据
    @Delete("delete from questionnaire_text where questionnaire_id = #{questionId} and text_id = #{questionId}")
    void deleteJointDataByQuestionnaireIdAndQuestionId(Long questionnaireId, Long questionId);

    // 删除问题后将seqNum-1
    @Update("update questionnaire_text set seq_num = seq_num-1 where questionnaire_id = #{questionnaireId} and seq_num > #{seqNum}")
    void minusQuestionSeq(Long questionnaireId, Integer seqNum);

    // 新增问题后将seqNum+1
    @Update("update questionnaire_text set seq_num = seq_num+1 where questionnaire_id = #{questionnaireId} and seq_num >= #{seqNum}")
    void addQuestionSeq(Long questionnaireId, Integer seqNum);

    // 查找表数据
    @Select("select * from table_content where id = #{id} and question_id = #{questionId}")
    TableContent selectTableDataById(Long id, Long questionId);

    // 更新表
    void updateTableContent(TableContent ta);

    // 更新时删除多余表数据
    void deleteTableDataByIdAndQuestionId(Long questionId, List<Long> tableIds);
}
