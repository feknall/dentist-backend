package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.user.UserImageOutputDto;
import ir.beheshti.dandun.base.user.dto.user.UserInfoInputDto;
import ir.beheshti.dandun.base.user.dto.user.UserInfoOutputDto;
import ir.beheshti.dandun.base.user.service.LoginAndSignUpService;
import ir.beheshti.dandun.base.user.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Tag(name = "User requests for profile", description = "managing user profile such as basic information or personal photo and ...")
@RestController
@RequestMapping(path = "/api/v1/user/profile")
public class UserProfileController {

    private final LoginAndSignUpService loginAndSignUpService;
    private final ProfileService profileService;

    public UserProfileController(LoginAndSignUpService loginAndSignUpService, ProfileService profileService) {
        this.loginAndSignUpService = loginAndSignUpService;
        this.profileService = profileService;
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

    @DeleteMapping(path = "/photo")
    public ResponseEntity<BaseOutputDto> removeUserInfoPhoto() {
        profileService.removeUserInfoPhoto();
        return ResponseEntity.ok(new BaseOutputDto("user photo removed successfully"));
    }

    @PostMapping(path = "/photo")
    public ResponseEntity<BaseOutputDto> uploadUserInfoPhoto(@RequestParam("photo") MultipartFile file) {
        profileService.updateUserInfoPhoto(file);
        return ResponseEntity.ok(new BaseOutputDto("user photo updated successfully"));
    }

    @GetMapping(path = "/photo")
    public ResponseEntity<UserImageOutputDto> getUserInfoImage() {
        return ResponseEntity.ok(profileService.getUserInfoImage());
    }
}
