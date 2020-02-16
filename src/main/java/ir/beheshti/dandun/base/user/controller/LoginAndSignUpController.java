package ir.beheshti.dandun.base.user.controller;

import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.login.LoginInputDto;
import ir.beheshti.dandun.base.user.dto.login.LoginOutputDto;
import ir.beheshti.dandun.base.user.dto.signup.SignUpInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsVerificationInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsVerificationOutputDto;
import ir.beheshti.dandun.base.user.service.LoginAndSignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/user")
public class LoginAndSignUpController {

    @Autowired
    private LoginAndSignUpService loginAndSignUpService;

    @PostMapping(path = "/sms")
    public ResponseEntity<BaseOutputDto> sendSmsForUser(@Valid @RequestBody SmsInputDto input) {
        loginAndSignUpService.sendVerificationCodeBySms(input);
        return ResponseEntity.ok(new BaseOutputDto("sms sent successfully"));
    }

    @PostMapping(path = "/sms/verify")
    public ResponseEntity<SmsVerificationOutputDto> verifyPhoneNumber(@Valid @RequestBody SmsVerificationInputDto inputDto) {
        return ResponseEntity.ok(loginAndSignUpService.verifyPhoneNumber(inputDto));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<BaseOutputDto> signUp(@Valid @RequestBody SignUpInputDto input) {
        loginAndSignUpService.signUp(input);
        return ResponseEntity.ok(new BaseOutputDto("user added successfully"));
    }

}
