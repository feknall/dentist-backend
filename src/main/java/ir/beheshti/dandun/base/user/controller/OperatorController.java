package ir.beheshti.dandun.base.user.controller;

import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientStateInputDto;
import ir.beheshti.dandun.base.user.dto.operator.PatientStateOutputDto;
import ir.beheshti.dandun.base.user.service.OperatorService;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/operator")
public class OperatorController {

    @Autowired
    private OperatorService operatorService;

    @PostMapping(path = "/patient")
    public ResponseEntity<BaseOutputDto> fillPatientState(@Valid @RequestBody PatientStateInputDto patientStateInputDto) {
        operatorService.fillPatientState(patientStateInputDto);
        return ResponseEntity.ok(new BaseOutputDto("patient state completed successfully"));
    }

    @GetMapping(path = "/patient/{patientId}")
    public ResponseEntity<PatientOutputDto> getPatientState(@PathVariable int patientId) {
        return ResponseEntity.ok(operatorService.getPatientState(patientId));
    }

    @GetMapping(path = "/patient")
    public ResponseEntity<List<PatientOutputDto>> getPatientsList(@RequestParam(required = false) PatientStateType patientStateType) {
        return ResponseEntity.ok(operatorService.getPatientsList(patientStateType));
    }
}

