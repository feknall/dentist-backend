package ir.beheshti.dandun.base.user.controller;

import ir.beheshti.dandun.base.user.dto.question.QuestionOutputDto;
import ir.beheshti.dandun.base.user.service.EssentialQuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<QuestionOutputDto> getById(@PathVariable int questionId) {
        return ResponseEntity.ok(essentialQuestionService.getById(questionId));
    }
}
