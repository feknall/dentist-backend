package ir.beheshti.dandun.base.user.controller;

import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.question.AnswerInputDto;
import ir.beheshti.dandun.base.user.dto.question.AnswerOutputDto;
import ir.beheshti.dandun.base.user.dto.question.QuestionOutputDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/question")
public class QuestionController {

    @GetMapping
    public ResponseEntity<List<QuestionOutputDto>> getAllQuestions() {
        return null;
    }

    @GetMapping(path = "/{questionId}")
    public ResponseEntity<QuestionOutputDto> getById(@PathVariable Integer questionId) {
        return null;
    }
}
