package com.foufou.mapper;

import com.foufou.dto.TextQuestionDTO;
import com.foufou.entity.TextQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TextQuestionMapper {
    // 根据id删除填空题
    void deleteTextQuestionById(List<Long> textQuestionIds);

    // 根据id获取填空题信息
    List<TextQuestion> selectByQuestionIds(List<Long> textQuestionIds);
}
