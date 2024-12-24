package com.foufou.service.impl;

import com.foufou.dto.*;
import com.foufou.entity.*;
import com.foufou.mapper.*;
import com.foufou.service.AnswerService;
import com.foufou.vo.SelectAnswerVO;
import com.foufou.vo.SingleTableAnswer;
import com.foufou.vo.TableAnswerVO;
import com.foufou.vo.TextAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private SelectQuestionMapper selectQuestionMapper;
    @Autowired
    private TextQuestionMapper textQuestionMapper;
    @Autowired
    private SelectQuestionAnswerMapper selectQuestionAnswerMapper;
    @Autowired
    private TextQuestionAnswerMapper textQuestionAnswerMapper;
    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Override
    public void addAnswer(List<AnswerDTO> answerDTOs) {
        answerDTOs.forEach(answerDTO -> {
            if (answerDTO.getQuestionType() == 0) { // 选择题
                answerDTO.getAnswerContent().forEach(answerContentDTO -> {
                    if (null == answerDTO.getAnswerContent().get(0).getId()) {  //普通选择题
                        SelectAnswer selectAnswer = SelectAnswer.builder()
                                .selectInnerId(null)
                                .selectId(answerDTO.getQuestionId())
                                .questionnaireId(answerDTO.getQuestionnaireId())
                                .stuName(answerDTO.getStuName())
                                .content(answerContentDTO.getAnswer())
                                .build();
                        selectQuestionAnswerMapper.addAnswer(selectAnswer);
                    } else {    //嵌套
                        SelectAnswer selectAnswer = SelectAnswer.builder()
                                .selectInnerId(answerContentDTO.getId())
                                .selectId(answerDTO.getQuestionId())
                                .questionnaireId(answerDTO.getQuestionnaireId())
                                .stuName(answerDTO.getStuName())
                                .content(answerContentDTO.getAnswer())
                                .build();
                        selectQuestionAnswerMapper.addAnswer(selectAnswer);
                    }

                });

            } else if (answerDTO.getQuestionType() == 1) {  //填空题
                if (null == answerDTO.getAnswerContent().get(0).getId()) {  // 精简填空题
                    TextAnswer textAnswer = TextAnswer.builder()
                            .textId(answerDTO.getQuestionId())
                            .questionnaireId(answerDTO.getQuestionnaireId())
                            .stuName(answerDTO.getStuName())
                            .tableId(null)
                            .content(answerDTO.getAnswerContent().get(0).getAnswer())
                            .build();
                    textQuestionAnswerMapper.addTextAnswer(textAnswer);
                } else {    // 表题
                    answerDTO.getAnswerContent().forEach(answerContentDTO -> {
                        TextAnswer textAnswer = TextAnswer.builder()
                                .textId(answerDTO.getQuestionId())
                                .questionnaireId(answerDTO.getQuestionnaireId())
                                .stuName(answerDTO.getStuName())
                                .tableId(answerContentDTO.getId())
                                .content(answerContentDTO.getAnswer())
                                .build();
                        textQuestionAnswerMapper.addTableAnswer(textAnswer);
                    });
                }
            }
        });

        //问卷填写次数+1
        questionnaireMapper.updateFillCount(answerDTOs.get(0).getQuestionnaireId());
    }

    @Override
    public List<TextAnswerVO> getTextResult(Long id) {
        List<TextAnswerVO> textAnswerList = new ArrayList<>();

        // 根据问卷id获取关联表中的问题id和seqNum
        List<TextJointData> textJointDataList = textQuestionMapper.getIdAndSeqNumByQuestionnaireId(id);

        // 根据问题id在table_content表中差找是否有关联的数据
        for (TextJointData textJointData : textJointDataList) {
            Long questionId = textJointData.getTextId();
            Integer seqNum = textJointData.getSeqNum();

            if (!textQuestionMapper.selectTableDataByQuestionId(questionId).isEmpty()) {     // 是表题
                continue;
            }

            TextAnswerVO textAnswerVO = new TextAnswerVO();
            textAnswerVO.setSeqNum(seqNum);

            // 对没有关联数据的问题id，在text_question中获取title
            List<Long> ids = new ArrayList<>();
            ids.add(questionId);
            List<TextQuestion> textQuestions = textQuestionMapper.selectByQuestionIds(ids);
            textAnswerVO.setQuestionTitle(textQuestions.get(0).getQuestionTitle());

            // 根据问题id获取答案
            List<String> answers = textQuestionAnswerMapper.selectAnswerBy(questionId);
            textAnswerVO.setAnswers(answers);

            textAnswerList.add(textAnswerVO);
        }

        return textAnswerList;
    }

    @Override
    public List<TableAnswerVO> getTableResult(Long id) {
        List<TableAnswerVO> tableAnswerList = new ArrayList<>();

        // 根据问卷id获取关联表中的问题id和seqNum
        List<TextJointData> textJointDataList = textQuestionMapper.getIdAndSeqNumByQuestionnaireId(id);

        // 根据问题id在table_content表中差找是否有关联的数据
        for (TextJointData textJointData : textJointDataList) {
            Long questionId = textJointData.getTextId();

            // 根据问题id获取表数据
            List<TableContentDTO> tableContentDTOList = textQuestionMapper.selectTableDataByQuestionId(questionId);

            if (tableContentDTOList.isEmpty()) {     // 是填空题
                continue;
            }

            TableAnswerVO tableAnswerVO = new TableAnswerVO();
            // 设置seqNum
            tableAnswerVO.setSeqNum(textJointData.getSeqNum());

            // 对有关联数据的问题id，在text_question中获取title
            List<Long> ids = new ArrayList<>();
            ids.add(questionId);
            List<TextQuestion> textQuestions = textQuestionMapper.selectByQuestionIds(ids);
            tableAnswerVO.setQuestionTitle(textQuestions.get(0).getQuestionTitle());

            // 根据问题id获取答案
            List<SingleTableAnswer> singleTableAnswerList = new ArrayList<>();
            tableContentDTOList.forEach(tableContentDTO -> {
                SingleTableAnswer singleTableAnswer = SingleTableAnswer.builder()
                        .col(tableContentDTO.getCol())
                        .line(tableContentDTO.getLine())
                        .build();
                List<String> contents = textQuestionAnswerMapper.getAveAnswerFromTableByQuestionnaireIdAndTableId(id, tableContentDTO.getId());
                // 获取平均值
                double aveContent = 0.0;
                if (!contents.isEmpty()) {
                    for (String content : contents) {
                        aveContent = Double.parseDouble(content) / contents.size();
                    }
                }
                singleTableAnswer.setContent(aveContent);

                singleTableAnswerList.add(singleTableAnswer);
            });

            tableAnswerVO.setSigleTableAnswerList(singleTableAnswerList);

            tableAnswerList.add(tableAnswerVO);
        }

        return tableAnswerList;
    }

    @Override
    public List<SelectAnswerVO> getSelectResult(Long id) {

        List<SelectAnswerVO> selectAnswerVOList = new ArrayList<>();

        // 根据问卷id获取问题id和seqNum
        List<SelectJointData> selectJointDataList = selectQuestionMapper.getIdAndSeqNumByQuestionnaireId(id);

        for (SelectJointData selectJointData : selectJointDataList) {   // 对每一个问题进行分析

            SelectAnswerVO selectAnswerVO = SelectAnswerVO.builder()
                    .seqNum(selectJointData.getSeqNum())
                    .build();

            Long questionId = selectJointData.getSelectId();
//            List<Long> ids = new ArrayList<>();
//            ids.add(questionId);

            // 根据问题id获取title
            SelectQuestion selectQuestion = selectQuestionMapper.selectQuestionById(questionId);
            selectAnswerVO.setQuestionTitle(selectQuestion.getQuestionTitle());

            // 根据问题id获取optionList
            List<Option> optionList = selectQuestionMapper.selectOptionsByQuestionId(questionId);
            List<String> optionContent = new ArrayList<>();
            optionList.forEach(option -> optionContent.add(option.getContent()));


            // 根据问题id获取内部嵌套选择题的id和detail
            List<SelectInnerQuestion> selectInnerQuestionList = selectQuestionMapper.selectInnerQuestionBySelectQuestionId(questionId);

            if (selectInnerQuestionList.isEmpty()) {    // 先判断是否存在id
                // 不存在嵌套id，根据问卷id、问题id获取答案
                List<SelectAnswer> selectAnswerList = selectQuestionAnswerMapper.getAnswerByQuestionnaireIdAndQuestionId(id, questionId);

                List<Integer> counts = new ArrayList<>();

                // 计算counts
                for (String option : optionContent) {
                    Integer count = 0;
                    if (!selectAnswerList.isEmpty()) {
                        for (SelectAnswer selectAnswer : selectAnswerList) {
                            if (selectAnswer.getContent().equals(option)) {
                                count++;
                            }
                        }
                    }
                    counts.add(count);
                }

                List<SelectInnerAnswerDTO> selectInnerAnswerDTOList = new ArrayList<>();
                SelectInnerAnswerDTO selectInnerAnswerDTO = new SelectInnerAnswerDTO(null, optionContent, null, counts);
                selectInnerAnswerDTOList.add(selectInnerAnswerDTO);
                selectAnswerVO.setSelectInnerAnswerDTOList(selectInnerAnswerDTOList);
                selectAnswerVO.setScore(null);

            } else {

                // seqNum和title已经备好，需要对每个大选择题的score和selectInnerAnswerDTOList赋值

                // 由内部嵌套选择题的id循环
                // 根据问卷id、问题id、内部嵌套选择题id获取contentList
                // 计算counts(结合optionList的Size，计算各个选项的数量)

                List<SelectInnerAnswerDTO> selectInnerAnswerDTOList = new ArrayList<>();

                for (SelectInnerQuestion selectInnerQuestion : selectInnerQuestionList) {
                    // 内部嵌套选择题的id和detail已经得到
                    SelectInnerAnswerDTO selectInnerAnswerDTO = new SelectInnerAnswerDTO();
                    //设置选项
                    selectInnerAnswerDTO.setOptionContent(optionContent);

                    Long selectInnerId = selectInnerQuestion.getId();

                    // 设置detail
                    selectInnerAnswerDTO.setQuestionDetails(selectInnerQuestion.getQuestionDetails());

                    // 获取答案
                    List<SelectAnswer> selectAnswerList = selectQuestionAnswerMapper.getAnswerByQuestionnaireIdAndQuestionIdAndInnerId(id, questionId, selectInnerId);
                    List<Integer> counts = new ArrayList<>();
                    for (String option : optionContent) {
                        Integer count = 0;
                        if (!selectAnswerList.isEmpty()) {
                            for (SelectAnswer selectAnswer : selectAnswerList) {
                                if (selectAnswer.getContent().equals(option)) {
                                    count++;
                                }
                            }
                        }
                        counts.add(count);
                    }

                    selectInnerAnswerDTO.setCounts(counts);

                    // 判断是否需要计算evaluation
                    if (optionContent.contains("非常满意")
                            && optionContent.contains("比较满意")
                            && optionContent.contains("一般")
                            && optionContent.contains("不太满意")
                            && optionContent.contains("很不满意")) {
                        double evaluation = 0.0;
                        if (counts.get(0) + counts.get(1) + counts.get(2) + counts.get(3) + counts.get(4) != 0) {
                            evaluation = (0.95 * counts.get(0) + 0.80 * counts.get(1) + 0.65 * counts.get(2) + 0.50 * counts.get(3) + 0.35 * counts.get(4))
                                    / (counts.get(0) + counts.get(1) + counts.get(2) + counts.get(3) + counts.get(4));
                        }
                        /**
                         * BigDecimal bd = new BigDecimal(value);
                         *         bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP); // 四舍五入
                         *         double result = bd.doubleValue();
                         */
                        BigDecimal bd = new BigDecimal(evaluation);
                        selectInnerAnswerDTO.setEvaluationScore(bd.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
                    } else {
                        selectInnerAnswerDTO.setEvaluationScore(null);
                    }

                    selectInnerAnswerDTOList.add(selectInnerAnswerDTO);

                }

                selectAnswerVO.setSelectInnerAnswerDTOList(selectInnerAnswerDTOList);


                // 计算SelectAnswerVO中的score(取最小)，普通选择题设为null
                if (optionContent.contains("非常满意")
                        && optionContent.contains("比较满意")
                        && optionContent.contains("一般")
                        && optionContent.contains("不太满意")
                        && optionContent.contains("很不满意")) {
                    double min = 100.0;
                    for (SelectInnerAnswerDTO selectInnerAnswerDTO : selectAnswerVO.getSelectInnerAnswerDTOList()){
                        if (selectInnerAnswerDTO.getEvaluationScore() < min) {
                            min = selectInnerAnswerDTO.getEvaluationScore();
                        }
                    }
                    selectAnswerVO.setScore(min);
                } else {
                    selectAnswerVO.setScore(null);
                }

            }


            selectAnswerVOList.add(selectAnswerVO);
        }

        return selectAnswerVOList;
    }

}
