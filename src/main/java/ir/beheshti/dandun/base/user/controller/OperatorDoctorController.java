package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.DoctorOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.DoctorStateInputDto;
import ir.beheshti.dandun.base.user.dto.question.UserQuestionAnswerOutputDto;
import ir.beheshti.dandun.base.user.service.EssentialQuestionService;
import ir.beheshti.dandun.base.user.service.OperatorService;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Operator Requests about Doctor", description = "Apis to manage doctors as operator")
@RestController
@RequestMapping(path = "/api/v1/operator/doctor")
public class OperatorDoctorController {

    private final OperatorService operatorService;
    private final EssentialQuestionService essentialQuestionService;

    public OperatorDoctorController(EssentialQuestionService essentialQuestionService, OperatorService operatorService) {
        this.essentialQuestionService = essentialQuestionService;
        this.operatorService = operatorService;
    }

    @GetMapping(path = "/{doctorId}/answer")
    public ResponseEntity<List<UserQuestionAnswerOutputDto>> getUserAnswers(@Valid @PathVariable int doctorId) {
        return ResponseEntity.ok(essentialQuestionService.getUserAnswersByOperator(doctorId));
    }

    @PostMapping
    public ResponseEntity<BaseOutputDto> fillDoctorState(@Valid @RequestBody DoctorStateInputDto doctorStateInputDto) {
        operatorService.fillDoctorState(doctorStateInputDto);
        return ResponseEntity.ok(new BaseOutputDto("doctor state completed successfully"));
    }

    @GetMapping
    public ResponseEntity<List<DoctorOutputDto>> getDoctorsList(@RequestParam(required = false) DoctorStateType doctorStateType) {
        return ResponseEntity.ok(operatorService.getDoctorList(doctorStateType));
    }

    @GetMapping(path = "/{doctorId}")
    public ResponseEntity<DoctorOutputDto> getDoctorState(@PathVariable int doctorId) {
        return ResponseEntity.ok(operatorService.getDoctorStateByOperator(doctorId));
    }
}

