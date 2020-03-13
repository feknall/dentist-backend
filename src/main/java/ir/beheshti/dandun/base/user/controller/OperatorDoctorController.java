package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.DoctorOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.DoctorStateInputDto;
import ir.beheshti.dandun.base.user.dto.question.ImageAnswerOutputDto;
import ir.beheshti.dandun.base.user.dto.question.QuestionOutputDto;
import ir.beheshti.dandun.base.user.dto.question.UserQuestionAnswerOutputDto2;
import ir.beheshti.dandun.base.user.service.EssentialQuestionService;
import ir.beheshti.dandun.base.user.service.OperatorService;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import ir.beheshti.dandun.base.user.util.QuestionOwnerType;
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

    @GetMapping(path = "/question")
    public ResponseEntity<List<QuestionOutputDto>> getAll() {
        return ResponseEntity.ok(essentialQuestionService.getAllQuestionsByQuestionOwner(QuestionOwnerType.Doctor));
    }

    @GetMapping(path = "/{doctorId}/answer")
    public ResponseEntity<List<UserQuestionAnswerOutputDto2>> getUserAnswers(@Valid @PathVariable int doctorId) {
        return ResponseEntity.ok(operatorService.getUserAnswers(doctorId));
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

    @GetMapping(path = "/{doctorId}/answer/image/question/{questionId}")
    public ResponseEntity<List<ImageAnswerOutputDto>> getUserImageAnswer(@PathVariable int doctorId, @PathVariable int questionId) {
        return ResponseEntity.ok(essentialQuestionService.getUserImageAnswerByOperator(doctorId, questionId));
    }
}

