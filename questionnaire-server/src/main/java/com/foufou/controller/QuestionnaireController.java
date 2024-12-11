package com.foufou.controller;

import com.foufou.constant.MessageConstant;
import com.foufou.dto.QuestionnaireDTO;
import com.foufou.dto.StuDTO;
import com.foufou.exception.StuIdError;
import com.foufou.results.PageResult;
import com.foufou.results.Result;
import com.foufou.service.QuestionnaireService;
import com.foufou.vo.QuestionnaireDetailVO;
import com.foufou.vo.QuestionnaireVO;
import com.foufou.vo.StuQuestionnaireDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/paper")
public class QuestionnaireController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @PostMapping("/addPaper")
    public Result createPaper(@RequestBody QuestionnaireDTO questionnaireDTO) {
        log.info("创建问卷:{}",questionnaireDTO.getTitle());
        questionnaireService.createPaper(questionnaireDTO);
        return Result.success();
    }

    @DeleteMapping("/deletePaper/{id}")
    public Result deletePaper(@PathVariable Long id) {
        log.info("删除id为{}的问卷",id);
        questionnaireService.deletePaper(id);
        return Result.success();
    }

    @PutMapping("/updatePaper")
    public Result updatePaper(@RequestBody QuestionnaireDTO questionnaireDTO) {
        log.info("更新问卷");
        questionnaireService.updatePaper(questionnaireDTO);
        return Result.success();
    }

    @GetMapping("/getUserPapers/{userId}")
    public Result<PageResult> page(String title,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @PathVariable Long userId) {
        log.info("根据用户id分页查询:第{}页,共{}条数据",page,pageSize);
        PageResult pageResult = questionnaireService.pageSelect(title, page,pageSize, userId);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<QuestionnaireVO> getPaper(@PathVariable Long id) {
        log.info("根据id-{}回显问卷数据",id);
        QuestionnaireVO questionnaireVO = questionnaireService.getPaper(id);
        return Result.success(questionnaireVO);
    }

    @GetMapping("/checkPaper/{id}")
    public Result<QuestionnaireDetailVO> getWholePaper(@PathVariable Long id) {
        log.info("根据id-{}查看整张问卷",id);
        QuestionnaireDetailVO questionnaireDetail = questionnaireService.getWholePaper(id);
        return Result.success(questionnaireDetail);
    }

    @GetMapping("/stu/getPaper")
    public Result<StuQuestionnaireDetailVO> getPaperToStu(@RequestBody StuDTO stuDTO) {
        if (stuDTO.getStuId().length() != 8) {
            throw new StuIdError(MessageConstant.STU_ID_ERROR);
        }
        Integer grade = Integer.parseInt("20" + stuDTO.getStuId().substring(0, 2));

        StuQuestionnaireDetailVO stuQuestionnaireDetailVO = questionnaireService.getPaperToStu(grade, stuDTO.getMajor());
        return Result.success(stuQuestionnaireDetailVO);
    }
}
