package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.OperatorLoginInputDto;
import ir.beheshti.dandun.base.user.dto.operator.OperatorLoginOutputDto;
import ir.beheshti.dandun.base.user.dto.signup.SignUpInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsVerificationInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsVerificationOutputDto;
import ir.beheshti.dandun.base.user.service.LoginRegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Login And Register", description = "Things like login, register, verify, sms and so on")
@RestController
@RequestMapping(path = "/api/v1/user/login-register/")
public class LoginRegisterController {

    private final LoginRegisterService loginRegisterService;

    public LoginRegisterController(LoginRegisterService loginRegisterService) {
        this.loginRegisterService = loginRegisterService;
    }

    @PostMapping(path = "/sms")
    public ResponseEntity<BaseOutputDto> sendSmsForUser(@Valid @RequestBody SmsInputDto input) {
        loginRegisterService.sendVerificationCodeBySms(input);
        return ResponseEntity.ok(new BaseOutputDto("sms sent successfully"));
    }

    @PostMapping(path = "/sms/verify")
    public ResponseEntity<SmsVerificationOutputDto> verifyPhoneNumber(@Valid @RequestBody SmsVerificationInputDto inputDto) {
        return ResponseEntity.ok(loginRegisterService.verifyPhoneNumber(inputDto));
    }

    @PostMapping(path = "/operator/login")
    public ResponseEntity<OperatorLoginOutputDto> loginOperator(@Valid @RequestBody OperatorLoginInputDto operatorLoginInputDto) {
        return ResponseEntity.ok(loginRegisterService.loginOperator(operatorLoginInputDto));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<BaseOutputDto> signUp(@Valid @RequestBody SignUpInputDto input) {
        loginRegisterService.signUp(input);
        return ResponseEntity.ok(new BaseOutputDto("user added successfully"));
    }
}
