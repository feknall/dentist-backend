package ir.beheshti.dandun.base.user.controller;

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

@RestController
@RequestMapping(path = "/api/v1/operator")
public class OperatorController {

    private final OperatorService operatorService;
    private final EssentialQuestionService essentialQuestionService;

    public OperatorController(EssentialQuestionService essentialQuestionService, OperatorService operatorService) {
        this.essentialQuestionService = essentialQuestionService;
        this.operatorService = operatorService;
    }

    @PostMapping(path = "/patient")
    public ResponseEntity<BaseOutputDto> fillPatientState(@Valid @RequestBody PatientStateInputDto patientStateInputDto) {
        operatorService.fillPatientState(patientStateInputDto);
        return ResponseEntity.ok(new BaseOutputDto("patient state completed successfully"));
    }

    @GetMapping(path = "/patient/{patientId}/answer")
    public ResponseEntity<List<UserQuestionAnswerOutputDto>> getUserAnswers(@Valid @PathVariable int patientId) {
        return ResponseEntity.ok(essentialQuestionService.getUserAnswersByOperator(patientId));
    }

    @GetMapping(path = "/patient/{patientId}")
    public ResponseEntity<PatientOutputDto> getPatientState(@PathVariable int patientId) {
        return ResponseEntity.ok(operatorService.getPatientStateByOperator(patientId));
    }

    @GetMapping(path = "/patient")
    public ResponseEntity<List<PatientOutputDto>> getPatientsList(@RequestParam(required = false) PatientStateType patientStateType) {
        return ResponseEntity.ok(operatorService.getPatientsList(patientStateType));
    }
}

