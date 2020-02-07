package ir.beheshti.dandun.base.user.controller;

import ir.beheshti.dandun.base.user.dto.login.LoginInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsInputDto;
import ir.beheshti.dandun.base.user.service.LoginAndSignUpService;
import ir.beheshti.dandun.base.user.dto.login.LoginOutputDto;
import ir.beheshti.dandun.base.user.dto.signup.SignUpInputDto;
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
    public ResponseEntity<String> sendSmsForUser(@Valid @RequestBody SmsInputDto input) {
        loginAndSignUpService.sendCode(input);
        return ResponseEntity.ok("sms sent successfully");
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginOutputDto> login(@Valid @RequestBody LoginInputDto input) {
        return ResponseEntity.ok(loginAndSignUpService.login(input));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpInputDto input) {
        loginAndSignUpService.signUp(input);
        return ResponseEntity.ok("user added and sms sent successfully");
    }

}
