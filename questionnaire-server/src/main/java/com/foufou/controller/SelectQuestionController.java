package com.foufou.controller;

import com.foufou.dto.SelectQuestionDTO;
import com.foufou.results.Result;
import com.foufou.service.SelectQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/question/select")
public class SelectQuestionController {

    @Autowired
    private SelectQuestionService selectQuestionService;

    @PostMapping("/add")
    public Result<Long> addSelectQuestion(Long questionnaireId, Integer seqNum) {
        log.info("在问卷{}中新增问题",questionnaireId);
        Long questionId = selectQuestionService.addSelectQuestion(questionnaireId, seqNum);
        return Result.success(questionId);
    }

    @PutMapping("/update")
    public Result updateSelectQuestion(@RequestBody SelectQuestionDTO selectQuestionDTO) {
        log.info("更新选择题");
        selectQuestionService.updateQuestion(selectQuestionDTO);
        return Result.success();
    }

    @DeleteMapping("/{questionnaireId}/{questionId}")
    public Result deleteSelectQuestion(@PathVariable Long questionnaireId,
                                       @PathVariable Long questionId) {
        log.info("在问卷{}中删除选择题{}",questionnaireId, questionId);
        selectQuestionService.deleteQuestion(questionnaireId, questionId);
        return Result.success();
    }
}
