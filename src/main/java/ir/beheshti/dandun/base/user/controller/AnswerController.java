package ir.beheshti.dandun.base.user.controller;

import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.question.AnswerInputDto;
import ir.beheshti.dandun.base.user.dto.question.AnswerOutputDto;
import ir.beheshti.dandun.base.user.dto.question.QuestionOutputDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/answer")
public class AnswerController {

    @PostMapping
    public ResponseEntity<BaseOutputDto> fillAnswer(@RequestBody AnswerInputDto answerInputDto) {
        return null;
    }

    @GetMapping
    public ResponseEntity<AnswerOutputDto> getAll() {
        return null;
    }


}
