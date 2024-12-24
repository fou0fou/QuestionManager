package com.foufou.service.impl;

import com.foufou.dto.TextQuestionDTO;
import com.foufou.dto.TextSimpleQuestionDTO;
import com.foufou.entity.TableContent;
import com.foufou.entity.TextQuestion;
import com.foufou.mapper.SelectQuestionMapper;
import com.foufou.mapper.TextQuestionAnswerMapper;
import com.foufou.mapper.TextQuestionMapper;
import com.foufou.service.TextQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TextQuestionServiceImpl implements TextQuestionService {

    @Autowired
    private TextQuestionMapper textQuestionMapper;
    @Autowired
    private SelectQuestionMapper selectQuestionMapper;
    @Autowired
    private TextQuestionAnswerMapper textQuestionAnswerMapper;

    @Override
    public Long addTextQuestion(Long questionnaireId, Integer seqNum) {
        TextQuestion textQuestion = new TextQuestion();
        textQuestionMapper.insertQuestion(textQuestion);

        // 获取问题主键
        Long questionId = textQuestion.getId();

        // 更新比seqNum大的seqNum
        textQuestionMapper.addQuestionSeq(questionnaireId, seqNum);
        selectQuestionMapper.addQuestionSeq(questionnaireId, seqNum);

        // 在问卷-填空题关联表中插入数据
        textQuestionMapper.insertJointData(questionnaireId, questionId, seqNum);

        return questionId;
    }

    @Override
    public void updateTextQuestion(TextSimpleQuestionDTO textSimpleQuestionDTO) {
        TextQuestion textQuestion = TextQuestion.builder()
                .id(textSimpleQuestionDTO.getId())
                .questionTitle(textSimpleQuestionDTO.getQuestionTitle())
                .build();

        // 更新填空题
        textQuestionMapper.updateQuestion(textQuestion);
    }

    @Override
    public void updateTableQuestion(TextQuestionDTO textQuestionDTO) {
        TextQuestion textQuestion = TextQuestion.builder()
                .id(textQuestionDTO.getId())
                .questionTitle(textQuestionDTO.getQuestionTitle())
                .build();
        textQuestionMapper.updateQuestion(textQuestion);

        // 新建表结构
        List<Long> tableIds = new ArrayList<>();
        textQuestionDTO.getTableContentDTOList().forEach(tableContentDTO -> {

            TableContent tableContent = null;

            if (null != tableContentDTO.getId()) {
                // 根据id查找是否存在该数据
                tableContent = textQuestionMapper.selectTableDataById(tableContentDTO.getId(), textQuestion.getId());
            }

            if (null != tableContent) {     // 若存在，更新，并tableIds
                TableContent ta = TableContent.builder()
                        .questionId(textQuestion.getId())
                        .col(tableContentDTO.getCol())
                        .line(tableContentDTO.getLine())
                        .build();
                textQuestionMapper.updateTableContent(ta);

                tableIds.add(tableContent.getId());
            } else {    // 若不存在，插入，并tableIds
                tableContent = TableContent.builder()
                        .questionId(textQuestionDTO.getId())
                        .col(tableContentDTO.getCol())
                        .line(tableContentDTO.getLine())
                        .build();
                textQuestionMapper.insertTableContent(tableContent);

                tableIds.add(tableContent.getId());
            }

        });
        // 删除多余的表
        textQuestionMapper.deleteTableDataByIdAndQuestionId(textQuestionDTO.getId(), tableIds);
    }

    @Override
    public void deleteQuestionById(Long questionnaireId, Long questionId) {
        // 1.删除与questionId关联的填空题答案
        textQuestionAnswerMapper.deleteTextAnswerByQuestionId(questionId);

        // 2.删除与questionId关联的TableContent数据
        List<Long> ids = new ArrayList<>();
        ids.add(questionId);
        textQuestionMapper.deleteTableDataByQuestionId(ids);

        // 3.获取该问题的seqNum
        Integer seqNum = textQuestionMapper.getSeqNumByQuestionnaireIdAndQuestionId(questionnaireId, questionId);

        // 3.在问卷-填空关联表中删除相关数据
        textQuestionMapper.deleteJointDataByQuestionnaireIdAndQuestionId(questionnaireId, questionId);

        // 4.在两张关联表中查找比seqNum大的题目，将这几个题目的seqNum减去1
        textQuestionMapper.minusQuestionSeq(questionnaireId, seqNum);
        selectQuestionMapper.minusQuestionSeq(questionnaireId, seqNum);

        // 5.删除大填空题
        textQuestionMapper.deleteTextQuestionById(ids);

    }
}
