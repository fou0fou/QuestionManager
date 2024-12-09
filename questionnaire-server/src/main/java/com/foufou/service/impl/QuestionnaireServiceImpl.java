package com.foufou.service.impl;

import com.foufou.dto.*;
import com.foufou.entity.*;
import com.foufou.mapper.*;
import com.foufou.results.PageResult;
import com.foufou.service.QuestionnaireService;
import com.foufou.vo.QuestionnaireDetailVO;
import com.foufou.vo.QuestionnaireVO;
import com.foufou.vo.SelectQuestionVO;
import com.foufou.vo.TextQuestionVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

    @Autowired
    private QuestionnaireMapper questionnaireMapper;
    @Autowired
    private TextQuestionMapper textQuestionMapper;
    @Autowired
    private SelectQuestionMapper selectQuestionMapper;
    @Autowired
    private SelectQuestionAnswerMapper selectQuestionAnswerMapper;
    @Autowired
    private TextQuestionAnswerMapper textQuestionAnswerMapper;

    @Override
    public void createPaper(QuestionnaireDTO questionnaireDTO) {
        Questionnaire questionnaire = Questionnaire.builder().build();
        BeanUtils.copyProperties(questionnaireDTO, questionnaire);
        questionnaire.setFillCount(0);
        questionnaireMapper.insertPaper(questionnaire);
    }

    @Override
    public void deletePaper(Long id) {
        //先在问卷-题目表中查找题目id
        // TODO 这里在后期写完新增问题后要重新测试，查看是否能获取到问题id
        List<Long> selectQuestionIds = questionnaireMapper.selectByQuestionnaireId01(id);

        if (!selectQuestionIds.isEmpty()) { // 非空才能进行查找
            // 查找内部嵌套的选择题id
            List<Long> selectInnerQuestionIds = selectQuestionMapper.selectInnerQuestionIdBySelectQuestionIds(selectQuestionIds);

            // 根据问卷id删除选择答案(在这里删除答案是因为里面的问题字段与innerQuestion关联)
            selectQuestionAnswerMapper.deleteSelectAnswerByQuestionnaireId(id);

            // 删内部嵌套的选择题
            if (!selectInnerQuestionIds.isEmpty()) {
                selectQuestionMapper.deleteSelectInnerQuestionById(selectInnerQuestionIds);
            }
        }

        List<Long> textQuestionIds = questionnaireMapper.selectByQuestionnaireId02(id);

        // 根据问卷id删除填空答案
        textQuestionAnswerMapper.deleteTextAnswerByQuestionnaireId(id);

        // 先根据问卷id删问卷-问题关联表
        questionnaireMapper.deleteQuestionnaireJoinSelectQuestionByQuestionnaireId(id);
        questionnaireMapper.deleteQuestionnaireJoinTextQuestionByQuestionnaireId(id);

        // 删选项和大选择题
        if (!selectQuestionIds.isEmpty()) {
            selectQuestionMapper.deleteOptionById(selectQuestionIds);
            selectQuestionMapper.deleteSelectQuestionById(selectQuestionIds);
        }

        // 再删填空题
        if (!textQuestionIds.isEmpty()) {
            textQuestionMapper.deleteTableDataByQuestionId(textQuestionIds);
            textQuestionMapper.deleteTextQuestionById(textQuestionIds);
        }

        //删除问卷
        questionnaireMapper.deleteById(id);
    }

    @Override
    public void updatePaper(QuestionnaireDTO questionnaireDTO) {
        Questionnaire questionnaire = Questionnaire.builder().build();
        BeanUtils.copyProperties(questionnaireDTO, questionnaire);

        questionnaireMapper.updateQuestionnaire(questionnaire);
    }

    @Override
    public PageResult pageSelect(String title, Integer page, Integer pageSize, Long userId) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);

        // 执行查询
        Page<QuestionnaireVO> result = questionnaireMapper.list(title, userId);

        return new PageResult(result.getTotal(), result.getResult());
    }

    @Override
    public QuestionnaireVO getPaper(Long id) {
        Questionnaire questionnaire = questionnaireMapper.getPaperById(id);
        return new QuestionnaireVO(
                id,
                questionnaire.getCreateTime(),
                questionnaire.getUpdateTime(),
                questionnaire.getStartTime(),
                questionnaire.getEndTime(),
                questionnaire.getStatus(),
                questionnaire.getFillCount(),
                questionnaire.getDescription(),
                questionnaire.getTitle()
        );
    }

    @Override
    public QuestionnaireDetailVO getWholePaper(Long id) {

        // TODO 因为当前没有题目数据，具体测试需要到后面进行

        QuestionnaireDetailVO questionnaireDetailVO = new QuestionnaireDetailVO();

        // 先通过id获取问卷信息
        Questionnaire questionnaire = questionnaireMapper.getPaperById(id);
        BeanUtils.copyProperties(questionnaire, questionnaireDetailVO);

        // 获取问卷填空题的信息
        // 先在关联表中获得与问卷id关联的填空题id
        List<Long> textQuestionIds = questionnaireMapper.selectByQuestionnaireId02(id);
        if (!textQuestionIds.isEmpty()) {
            // 根据填空题id获取填空题信息
            List<TextQuestion> textQuestionList = textQuestionMapper.selectByQuestionIds(textQuestionIds);
            List<TextQuestionVO> textQuestionVOList = new ArrayList<>();
            textQuestionList.forEach(textQuestion -> {
                TextQuestionVO textQuestionVO = new TextQuestionVO();
                BeanUtils.copyProperties(textQuestion, textQuestionVO);

                // 先要通过填空题id查找是否在TableContent表中存在关联的数据
                List<TableContentDTO> tableContentDTOList = textQuestionMapper.selectTableDataByQuestionId(textQuestion.getId());
                if (!tableContentDTOList.isEmpty()) {
                    textQuestionVO.setTableContentDTOList(tableContentDTOList);
                } else {
                    textQuestionVO.setTableContentDTOList(null);
                }

                // 查找seqNum
                Integer seqNum = textQuestionMapper.getSeqNumByQuestionnaireIdAndQuestionId(id, textQuestion.getId());
                textQuestionVO.setSeqNum(seqNum);

                textQuestionVOList.add(textQuestionVO);
            });
            // 设置填空类信息
            questionnaireDetailVO.setTextQuestionList(textQuestionVOList);
        } else {
            questionnaireDetailVO.setTextQuestionList(null);
        }

        // 获取问卷选择题信息
        // 先在关联表中获得与问卷id关联的选择题id
        List<Long> selectQuestionIds = questionnaireMapper.selectByQuestionnaireId01(id);
        if (!selectQuestionIds.isEmpty()) {
            // 存储选择题信息
            List<SelectQuestionVO> selectQuestionList = new ArrayList<>();
            // 对每一个id操作
            selectQuestionIds.forEach(selectQuestionId -> {

                SelectQuestionVO selectQuestionVO = new SelectQuestionVO();

                // 根据id查询大选择题的title和selectType
                SelectQuestion selectQuestion = selectQuestionMapper.selectQuestionById(selectQuestionId);

                // 根据id和选择题id在关联表中查找seqNum
                Integer seqNum = selectQuestionMapper.getSeqNumByQuestionnaireIdAndQuestionId(id, selectQuestionId);

                // 根据id查询内部嵌套的选择题信息
                List<SelectInnerQuestion> selectInnerQuestions = selectQuestionMapper.selectInnerQuestionBySelectQuestionId(selectQuestionId);
                List<SelectInnerQuestionDTO> selectInnerQuestionDTOList;
                if (!selectInnerQuestions.isEmpty()) {
                    selectInnerQuestionDTOList = new ArrayList<>();
                    selectInnerQuestions.forEach(selectInnerQuestion -> {
                        selectInnerQuestionDTOList.add(new SelectInnerQuestionDTO(
                                selectInnerQuestion.getId(),
                                selectInnerQuestion.getQuestionDetails()));
                    });
                } else {        // 没有内部嵌套选择题
                    selectInnerQuestionDTOList =  null;
                }

                // 根据id查询选项
                List<Option> options = selectQuestionMapper.selectOptionsByQuestionId(selectQuestionId);
                List<OptionDTO> optionDTOList = new ArrayList<>();
                options.forEach(option -> {
                    optionDTOList.add(new OptionDTO(option.getSequence(), option.getContent()));
                });

                // 将查询到的信息总和到一个selectQuestion，并add到一个List中
                BeanUtils.copyProperties(selectQuestion, selectQuestionVO);
                selectQuestionVO.setTitle(selectQuestion.getQuestionTitle());
                selectQuestionVO.setSeqNum(seqNum);
                selectQuestionVO.setSelectInnerQuestionDTOList(selectInnerQuestionDTOList);
                selectQuestionVO.setOptionDTOList(optionDTOList);

                selectQuestionList.add(selectQuestionVO);
            });

            // 设置选择类信息
            questionnaireDetailVO.setSelectQuestionList(selectQuestionList);
        } else {
            questionnaireDetailVO.setSelectQuestionList(null);
        }

        return questionnaireDetailVO;
    }
}
