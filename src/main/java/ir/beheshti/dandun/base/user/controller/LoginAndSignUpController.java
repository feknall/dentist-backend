package ir.beheshti.dandun.base.user.controller;

import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.operator.OperatorLoginInputDto;
import ir.beheshti.dandun.base.user.dto.operator.OperatorLoginOutputDto;
import ir.beheshti.dandun.base.user.dto.signup.SignUpInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsVerificationInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsVerificationOutputDto;
import ir.beheshti.dandun.base.user.dto.user.UserInfoInputDto;
import ir.beheshti.dandun.base.user.dto.user.UserInfoOutputDto;
import ir.beheshti.dandun.base.user.service.LoginAndSignUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/user")
public class LoginAndSignUpController {

    private final LoginAndSignUpService loginAndSignUpService;

    public LoginAndSignUpController(LoginAndSignUpService loginAndSignUpService) {
        this.loginAndSignUpService = loginAndSignUpService;
    }

    @PostMapping(path = "/sms")
    public ResponseEntity<BaseOutputDto> sendSmsForUser(@Valid @RequestBody SmsInputDto input) {
        loginAndSignUpService.sendVerificationCodeBySms(input);
        return ResponseEntity.ok(new BaseOutputDto("sms sent successfully"));
    }

    @PostMapping(path = "/sms/verify")
    public ResponseEntity<SmsVerificationOutputDto> verifyPhoneNumber(@Valid @RequestBody SmsVerificationInputDto inputDto) {
        return ResponseEntity.ok(loginAndSignUpService.verifyPhoneNumber(inputDto));
    }

    @PostMapping(path = "/operator/login")
    public ResponseEntity<OperatorLoginOutputDto> loginOperator(@Valid @RequestBody OperatorLoginInputDto operatorLoginInputDto) {
        return ResponseEntity.ok(loginAndSignUpService.loginOperator(operatorLoginInputDto));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<BaseOutputDto> signUp(@Valid @RequestBody SignUpInputDto input) {
        loginAndSignUpService.signUp(input);
        return ResponseEntity.ok(new BaseOutputDto("user added successfully"));
    }

    @GetMapping(path = "/info")
    public ResponseEntity<UserInfoOutputDto> getUserInfo() {
        return ResponseEntity.ok(loginAndSignUpService.getUserInfo());
    }

    @PostMapping(path = "/info")
    public ResponseEntity<BaseOutputDto> updateUserInfo(@Valid @RequestBody UserInfoInputDto input) {
        loginAndSignUpService.updateUserInfo(input);
        return ResponseEntity.ok(new BaseOutputDto("user updated successfully"));
    }

    @DeleteMapping(path = "/info/photo")
    public ResponseEntity<BaseOutputDto> removeUserInfoPhoto() {
        loginAndSignUpService.removeUserInfoPhoto();
        return ResponseEntity.ok(new BaseOutputDto("user photo removed successfully"));
    }

    @PostMapping(path = "/info/photo")
    public ResponseEntity<BaseOutputDto> uploadUserInfoPhoto(@RequestParam("photo") MultipartFile file) {
        loginAndSignUpService.updateUserInfoPhoto(file);
        return ResponseEntity.ok(new BaseOutputDto("user photo updated successfully"));
    }

    @GetMapping(path = "/info/photo")
    public byte[] getUserInfoPhoto() {
        return loginAndSignUpService.getUserInfoPhoto();
    }

}
