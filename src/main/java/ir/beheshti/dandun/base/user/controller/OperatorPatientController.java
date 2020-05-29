package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientStateInputDto;
import ir.beheshti.dandun.base.user.dto.question.ImageAnswerOutputDto;
import ir.beheshti.dandun.base.user.dto.question.QuestionOutputDto;
import ir.beheshti.dandun.base.user.dto.question.UserQuestionAnswerOutputDto;
import ir.beheshti.dandun.base.user.service.EssentialQuestionService;
import ir.beheshti.dandun.base.user.service.OperatorService;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import ir.beheshti.dandun.base.user.util.QuestionOwnerType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Operator Requests about Patient", description = "Apis to manage patients as operator")
@RestController
@RequestMapping(path = "/api/v1/operator/patient")
public class OperatorPatientController {

    private final OperatorService operatorService;
    private final EssentialQuestionService essentialQuestionService;

    public OperatorPatientController(EssentialQuestionService essentialQuestionService, OperatorService operatorService) {
        this.essentialQuestionService = essentialQuestionService;
        this.operatorService = operatorService;
    }

    @GetMapping(path = "/question")
    public ResponseEntity<List<QuestionOutputDto>> getAll() {
        return ResponseEntity.ok(essentialQuestionService.getAllQuestionsByQuestionOwner(QuestionOwnerType.Patient));
    }

    @PostMapping
    public ResponseEntity<BaseOutputDto> changePatientState(@Valid @RequestBody PatientStateInputDto patientStateInputDto) {
        operatorService.changePatientState(patientStateInputDto);
        return ResponseEntity.ok(new BaseOutputDto("patient state completed successfully"));
    }

    @GetMapping(path = "/{patientId}/answer")
    public ResponseEntity<List<UserQuestionAnswerOutputDto>> getUserAnswers(@PathVariable int patientId) {
        return ResponseEntity.ok(operatorService.getUserAnswers(patientId));
    }

    @GetMapping(path = "/{patientId}/answer/image/question/{questionId}")
    public ResponseEntity<List<ImageAnswerOutputDto>> getUserImageAnswer(@PathVariable int patientId,
                                                                         @PathVariable int questionId) {
        return ResponseEntity.ok(essentialQuestionService.getUserImageAnswerByOperator(patientId, questionId));
    }

    @GetMapping(path = "/{patientId}")
    public ResponseEntity<PatientOutputDto> getPatientState(@PathVariable int patientId) {
        return ResponseEntity.ok(operatorService.getPatientStateByOperator(patientId));
    }

    @GetMapping
    public ResponseEntity<List<PatientOutputDto>> getPatientsList(@RequestParam(required = false) PatientStateType patientStateType) {
        return ResponseEntity.ok(operatorService.getPatientsList(patientStateType));
    }

}

