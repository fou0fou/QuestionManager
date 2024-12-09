package com.foufou.controller;

import com.foufou.dto.AnswerDTO;
import com.foufou.results.Result;
import com.foufou.service.AnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
