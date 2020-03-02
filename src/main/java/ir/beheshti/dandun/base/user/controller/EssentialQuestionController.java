package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.question.*;
import ir.beheshti.dandun.base.user.service.EssentialQuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Essential Questions", description = "Things related about getting and answering questions by patient or doctor")
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
        return ResponseEntity.ok(new BaseOutputDto("multiple-choice answer filled successfully"));
    }

    @PostMapping(path = "/answer/single-choice")
    public ResponseEntity<BaseOutputDto> fillSingleAnswer(@Valid @RequestBody SingleAnswerInputDto singleAnswerInputDto) {
        essentialQuestionService.fillSingleAnswer(singleAnswerInputDto);
        return ResponseEntity.ok(new BaseOutputDto("single-choice answer filled successfully"));
    }

    @PostMapping(path = "/answer/range")
    public ResponseEntity<BaseOutputDto> fillRangeAnswer(@Valid @RequestBody RangeAnswerInputDto rangeAnswerInputDto) {
        essentialQuestionService.fillRangeAnswer(rangeAnswerInputDto);
        return ResponseEntity.ok(new BaseOutputDto("range answer filled successfully"));
    }

    @PostMapping(path = "/answer/image")
    public ResponseEntity<BaseOutputDto> fillImageAnswer(@Valid @RequestBody ImageAnswerInputDto imageAnswerInputDto) {
        essentialQuestionService.fillImageAnswer(imageAnswerInputDto);
        return ResponseEntity.ok(new BaseOutputDto("image answer filled successfully"));
    }

    @GetMapping(path = "/answer/image")
    public ResponseEntity<ImageIdsOutputDto> getUserImageAnswerIds(@Valid @RequestBody ImageAnswerInputDto imageAnswerInputDto) {
        return ResponseEntity.ok(essentialQuestionService.getUserImageAnswerIds(imageAnswerInputDto));
    }

    @GetMapping(path = "/answer/image/{imageId}")
    public ResponseEntity<ImageAnswerOutputDto> getImageAnswer(@PathVariable Integer imageId) {
        return ResponseEntity.ok(essentialQuestionService.getUserImageAnswer(imageId));
    }

    @PostMapping(path = "/answer")
    public ResponseEntity<BaseOutputDto> fillAllAnswers(@Valid @RequestBody AllAnswerOpenDto allAnswerOpenDto) {
        essentialQuestionService.fillAllAnswers(allAnswerOpenDto);
        return ResponseEntity.ok(new BaseOutputDto("all answers completed successfully"));
    }

    @DeleteMapping(path = "/answer")
    public ResponseEntity<BaseOutputDto> deleteAllAnswers() {
        essentialQuestionService.deleteAllAnswers();
        return ResponseEntity.ok(new BaseOutputDto("all answers deleted successfully"));
    }

    @GetMapping(path = "/answer/user")
    public ResponseEntity<List<UserQuestionAnswerOutputDto>> getUserAnswers() {
        return ResponseEntity.ok(essentialQuestionService.getUserAnswersByUser());
    }

    @GetMapping(path = "/answer/user/is-complete")
    public ResponseEntity<IsCompleteAnswerOutputDto> isUserAnswersComplete() {
        return ResponseEntity.ok(essentialQuestionService.isUserAnswersComplete());
    }
}
