package com.foufou.controller;

import com.foufou.dto.TextQuestionDTO;
import com.foufou.dto.TextSimpleQuestionDTO;
import com.foufou.results.Result;
import com.foufou.service.TextQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/question/text")
public class TextQuestionController {

    @Autowired
    private TextQuestionService textQuestionService;

    @PostMapping("/add")
    public Result<Long> addTextQuestion(Long questionnaireId, Integer seqNum) {
        log.info("在问卷{}中新增问题",questionnaireId);
        Long questionId = textQuestionService.addTextQuestion(questionnaireId, seqNum);
        return Result.success(questionId);
    }

    @PutMapping("/updateText")
    public Result updateText(@RequestBody TextSimpleQuestionDTO textSimpleQuestionDTO) {
        log.info("更新填空题:{}",textSimpleQuestionDTO);
        textQuestionService.updateTextQuestion(textSimpleQuestionDTO);
        return Result.success();
    }

    @PutMapping("/updateTable")
    public Result updateTable(@RequestBody TextQuestionDTO textQuestionDTO) {
        log.info("更新表题:{}",textQuestionDTO);
        textQuestionService.updateTableQuestion(textQuestionDTO);
        return Result.success();
    }

    @DeleteMapping("/{questionnaireId}/{questionId}")
    public Result deleteQuestion(@PathVariable Long questionnaireId,
                                 @PathVariable Long questionId) {
        log.info("在问卷{}中删除id为{}的填空题", questionnaireId, questionId);
        textQuestionService.deleteQuestionById(questionnaireId, questionId);
        return Result.success();
    }
}
