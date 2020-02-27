package ir.beheshti.dandun.base.user.controller;

import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.question.*;
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

    @PostMapping(path = "/answer")
    public ResponseEntity<BaseOutputDto> fillAllAnswers(@Valid @RequestBody AllAnswerOpenDto allAnswerOpenDto) {
        essentialQuestionService.fillAllAnswers(allAnswerOpenDto);
        return ResponseEntity.ok(new BaseOutputDto("all answers completed successfully"));
    }

    @GetMapping(path = "/answer/user")
    public ResponseEntity<List<UserQuestionAnswerOutputDto>> getUserAnswers() {
        return ResponseEntity.ok(essentialQuestionService.getUserAnswers());
    }

    @GetMapping(path = "/answer/user/is-complete")
    public ResponseEntity<IsCompleteAnswerOutputDto> isUserAnswersComplete() {
        return ResponseEntity.ok(essentialQuestionService.isUserAnswersComplete());
    }

    //todo: create a section to fill user photos when answering essential questions.
}
