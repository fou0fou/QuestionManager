package com.foufou.controller;

import com.foufou.dto.AnswerDTO;
import com.foufou.results.Result;
import com.foufou.service.AnswerService;
import com.foufou.vo.SelectAnswerVO;
import com.foufou.vo.TableAnswerVO;
import com.foufou.vo.TextAnswerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping("/addAnswer")
    public Result addAnswer(@RequestBody List<AnswerDTO> answerDTOs) {
        log.info("增加答案");
        answerService.addAnswer(answerDTOs);
        return Result.success();
    }

    @GetMapping("/getTextResult/{id}")
    public Result<List<TextAnswerVO>> getTextStatistic(@PathVariable Long id) {
        log.info("对问卷{}的填空题分析",id);
        List<TextAnswerVO> textAnswerList = answerService.getTextResult(id);
        return Result.success(textAnswerList);
    }

    @GetMapping("/getTableResult/{id}")
    public Result<List<TableAnswerVO>> getTableStatistic(@PathVariable Long id) {
        log.info("对问卷{}的表题分析",id);
        List<TableAnswerVO> tableAnswerList = answerService.getTableResult(id);
        return Result.success(tableAnswerList);
    }

    @GetMapping("/getSelectResult/{id}")
    public Result<List<SelectAnswerVO>> getSelectResult(@PathVariable Long id) {
        log.info("对问卷{}的选择题分析",id);
        List<SelectAnswerVO> selectAnswerList = answerService.getSelectResult(id);
        return Result.success(selectAnswerList);
    }
}
