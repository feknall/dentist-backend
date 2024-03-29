package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.notification.NotificationOutputDto;
import ir.beheshti.dandun.base.notification.NotificationService;
import ir.beheshti.dandun.base.socket.ChatOutputDto;
import ir.beheshti.dandun.base.socket.ChatService;
import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.dto.socket.MessageOutputDto;
import ir.beheshti.dandun.base.user.dto.user.*;
import ir.beheshti.dandun.base.user.service.LoginRegisterService;
import ir.beheshti.dandun.base.user.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "User requests for profile", description = "managing user profile such as basic information or personal photo and ...")
@RestController
@RequestMapping(path = "/api/v1/user/profile")
public class UserProfileController {

    @Autowired
    private LoginRegisterService loginRegisterService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping(path = "/info")
    public ResponseEntity<UserInfoOutputDto> getUserInfo() {
        return ResponseEntity.ok(loginRegisterService.getUserInfo());
    }

    @PostMapping(path = "/info")
    public ResponseEntity<BaseOutputDto> updateUserInfo(@Valid @RequestBody UserInfoInputDto input) {
        loginRegisterService.updateUserInfo(input);
        return ResponseEntity.ok(new BaseOutputDto("user updated successfully"));
    }

    @DeleteMapping(path = "/photo")
    public ResponseEntity<BaseOutputDto> removeUserInfoPhoto() {
        profileService.removeUserInfoPhoto();
        return ResponseEntity.ok(new BaseOutputDto("user photo removed successfully"));
    }

    @PostMapping(path = "/photo")
    public ResponseEntity<BaseOutputDto> uploadUserInfoPhoto(@Valid @RequestBody UserImageInputDto userImageInputDto) {
        profileService.updateUserInfoPhoto(userImageInputDto);
        return ResponseEntity.ok(new BaseOutputDto("user photo updated successfully"));
    }

    @GetMapping(path = "/photo")
    public ResponseEntity<UserImageOutputDto> getUserInfoImage() {
        return ResponseEntity.ok(profileService.getUserInfoImage());
    }

    @PostMapping(path = "/notification-token")
    public ResponseEntity<BaseOutputDto> setUserNotificationToken(@Valid @RequestBody UserNotificationInputDto userNotificationInputDto) {
        profileService.setSetUserNotificationToken(userNotificationInputDto);
        return ResponseEntity.ok(new BaseOutputDto("notification token set successfully"));
    }

    @GetMapping(path = "/notification")
    public ResponseEntity<List<NotificationOutputDto>> getUserNotificationList() {
        return ResponseEntity.ok(notificationService.getUserNotificationList());
    }

    @GetMapping(path = "/logout")
    public ResponseEntity<BaseOutputDto> logoutFromAccount() {
        profileService.logout();
        return ResponseEntity.ok(new BaseOutputDto("logout successfully"));
    }


}
