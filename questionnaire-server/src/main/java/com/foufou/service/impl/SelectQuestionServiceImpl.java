package com.foufou.service.impl;

import com.foufou.dto.OptionDTO;
import com.foufou.dto.SelectQuestionDTO;
import com.foufou.entity.Option;
import com.foufou.entity.SelectInnerQuestion;
import com.foufou.entity.SelectQuestion;
import com.foufou.mapper.SelectQuestionAnswerMapper;
import com.foufou.mapper.SelectQuestionMapper;
import com.foufou.mapper.TextQuestionMapper;
import com.foufou.service.SelectQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SelectQuestionServiceImpl implements SelectQuestionService {

    @Autowired
    private SelectQuestionMapper selectQuestionMapper;
    @Autowired
    private TextQuestionMapper textQuestionMapper;
    @Autowired
    private SelectQuestionAnswerMapper selectQuestionAnswerMapper;

    @Override
    public Long addSelectQuestion(Long questionnaireId, Integer seqNum) {
        SelectQuestion selectQuestion = new SelectQuestion();
        // 插入空值
        selectQuestionMapper.insertQuestion(selectQuestion);

        // 获取问题主键id
        Long questionId = selectQuestion.getId();

        // 更新比seqNum大的seqNum
        textQuestionMapper.addQuestionSeq(questionnaireId, seqNum);
        selectQuestionMapper.addQuestionSeq(questionnaireId, seqNum);

        // 在问卷-选择关联表中插入数据
        selectQuestionMapper.insertJointData(questionnaireId, questionId, seqNum);

        return questionId;
    }

    @Override
    public void updateQuestion(SelectQuestionDTO selectQuestionDTO) {
        SelectQuestion selectQuestion = new SelectQuestion(
                selectQuestionDTO.getId(),
                selectQuestionDTO.getTitle(),
                selectQuestionDTO.getSelectType());
        // 更新大选择题的信息
        selectQuestionMapper.updateSelectQuestion(selectQuestion);

        // 更新内部嵌套选择题信息(需要遍历)
        // 先要检查该内部嵌套选择题是否存在,以此来进行新增或修改
        List<Long> selectInnerQuestionIds = new ArrayList<>();
        if (!selectQuestionDTO.getSelectInnerQuestionDTOList().isEmpty()) {
            selectQuestionDTO.getSelectInnerQuestionDTOList().forEach(selectInnerQuestionDTO -> {
                if (selectInnerQuestionDTO.getId() == null) {
                    // 插入嵌套的问题
                    SelectInnerQuestion selectInnerQuestion = new SelectInnerQuestion(
                            null,
                            selectQuestionDTO.getId(),
                            selectInnerQuestionDTO.getQuestionDetails()
                    );
                    selectQuestionMapper.insertSelectInnerQuestion(selectInnerQuestion);
                    selectInnerQuestionIds.add(selectInnerQuestion.getId());
                } else {
                    // 更新嵌套的选择题
                    selectQuestionMapper.updateSelectInnerQuestion(new SelectInnerQuestion(
                            selectInnerQuestionDTO.getId(),
                            selectQuestionDTO.getId(),
                            selectInnerQuestionDTO.getQuestionDetails()
                    ));
                    // 表示此内部嵌套选择已更新
                    selectInnerQuestionIds.add(selectInnerQuestionDTO.getId());
                }
            });
            //  删除多余的内部嵌套
            selectQuestionMapper.deleteExtraInnerQuestions(selectQuestionDTO.getId(), selectInnerQuestionIds);
        }

        // 更新选项
        List<Integer> alreadyExistOptionSequences = new ArrayList<>();
        if (!selectQuestionDTO.getOptionDTOList().isEmpty()) {

            selectQuestionDTO.getOptionDTOList().forEach(optionDTO -> {
                // 先查找是否已存在选项
                Option option = selectQuestionMapper.selectOptionsBySequence(selectQuestionDTO.getId(), optionDTO.getSequence());
                if (option == null) {
                    // 需要插入选项
                    selectQuestionMapper.insertOption(new Option(
                            selectQuestionDTO.getId(),
                            optionDTO.getSequence(),
                            optionDTO.getContent()));

                    alreadyExistOptionSequences.add(optionDTO.getSequence());
                } else {
                    // 需要更新选项
                    selectQuestionMapper.updateOption(new Option(
                            selectQuestionDTO.getId(),
                            optionDTO.getSequence(),
                            optionDTO.getContent()));
                    // 存进去，表示该选项已更新
                    alreadyExistOptionSequences.add(option.getSequence());
                }
            });
            // 若存在多余的选项，删除
            selectQuestionMapper.deleteExtraOption(selectQuestion.getId(), alreadyExistOptionSequences);
        }

    }

    @Override
    public void deleteQuestion(Long questionnaireId, Long questionId) {
        // 1.删除与id关联的option
        List<Long> questionIds = new ArrayList<>();
        questionIds.add(questionId);
        selectQuestionMapper.deleteOptionById(questionIds);

        // 2.查找与id关联的InnerQuestion的id
        List<Long> selectInnerQuestionIds = selectQuestionMapper.selectInnerQuestionIdBySelectQuestionIds(questionIds);
        if (!selectInnerQuestionIds.isEmpty()) {
            // 3.删除与InnerQuestionId关联的答案
            selectQuestionAnswerMapper.deleteAnswerByInnerQuestionId(selectInnerQuestionIds);
            // 4.删除与id关联的InnerQuestion
            selectQuestionMapper.deleteSelectInnerQuestionById(selectInnerQuestionIds);
        }

        // 5.获取该问题的seqNum
        Integer seqNum = selectQuestionMapper.getSeqNumByQuestionnaireIdAndQuestionId(questionnaireId, questionId);

        // 6.在问卷-选择关联表中删除与id有关的数据行
        selectQuestionMapper.deleteJointTableData(questionId);

        // 7.在两张关联表中查找比seqNum大的题目，将这几个题目的seqNum减去1
        textQuestionMapper.minusQuestionSeq(questionnaireId, seqNum);
        selectQuestionMapper.minusQuestionSeq(questionnaireId, seqNum);

        // 8.删除大选择题
        selectQuestionMapper.deleteSelectQuestionById(questionIds);

    }
}
