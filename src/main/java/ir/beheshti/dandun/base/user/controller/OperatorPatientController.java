package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientStateInputDto;
import ir.beheshti.dandun.base.user.dto.question.UserQuestionAnswerOutputDto;
import ir.beheshti.dandun.base.user.service.EssentialQuestionService;
import ir.beheshti.dandun.base.user.service.OperatorService;
import ir.beheshti.dandun.base.user.util.PatientStateType;
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

    @PostMapping
    public ResponseEntity<BaseOutputDto> fillPatientState(@Valid @RequestBody PatientStateInputDto patientStateInputDto) {
        operatorService.fillPatientState(patientStateInputDto);
        return ResponseEntity.ok(new BaseOutputDto("patient state completed successfully"));
    }

    @GetMapping(path = "/{patientId}/answer")
    public ResponseEntity<List<UserQuestionAnswerOutputDto>> getUserAnswers(@Valid @PathVariable int patientId) {
        return ResponseEntity.ok(essentialQuestionService.getUserAnswersByOperator(patientId));
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
