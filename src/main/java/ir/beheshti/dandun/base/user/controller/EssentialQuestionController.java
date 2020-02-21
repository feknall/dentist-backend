package ir.beheshti.dandun.base.user.controller;

import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.question.MultipleChoiceAnswerInputDto;
import ir.beheshti.dandun.base.user.dto.question.OpenAnswerInputDto;
import ir.beheshti.dandun.base.user.dto.question.QuestionOutputDto;
import ir.beheshti.dandun.base.user.dto.question.TrueFalseAnswerInputDto;
import ir.beheshti.dandun.base.user.service.EssentialQuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/question")
public class EssentialQuestionController {

    private final EssentialQuestionService essentialQuestionService;

    public EssentialQuestionController(EssentialQuestionService essentialQuestionService) {
        this.essentialQuestionService = essentialQuestionService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionOutputDto>> getAll() {
        return ResponseEntity.ok(essentialQuestionService.getAll());
    }

    @GetMapping(path = "/{questionId}")
    public ResponseEntity<QuestionOutputDto> getById(@Valid @PathVariable int questionId) {
        return ResponseEntity.ok(essentialQuestionService.getById(questionId));
    }

    @PostMapping(path = "/answer/open")
    public ResponseEntity<BaseOutputDto> fillOpenAnswer(@Valid @RequestBody OpenAnswerInputDto openAnswerInputDto) {
        essentialQuestionService.fillOpenAnswer(openAnswerInputDto);
        return ResponseEntity.ok(new BaseOutputDto("open answer filled successfully"));
    }

    @PostMapping(path = "/answer/multiple-choice")
    public ResponseEntity<BaseOutputDto> fillMultipleChoiceAnswer(@Valid @RequestBody MultipleChoiceAnswerInputDto multipleChoiceAnswerInputDto) {
        essentialQuestionService.fillMultipleChoiceAnswer(multipleChoiceAnswerInputDto);
        return ResponseEntity.ok(new BaseOutputDto("multiple choice answer filled successfully"));
    }

    @PostMapping(path = "/answer/true-false")
    public ResponseEntity<BaseOutputDto> fillTrueFalseAnswer(@Valid @RequestBody TrueFalseAnswerInputDto trueFalseAnswerInputDto) {
        essentialQuestionService.fillTrueFalseAnswer(trueFalseAnswerInputDto);
        return ResponseEntity.ok(new BaseOutputDto("true false choice answer filled successfully"));
    }
}
