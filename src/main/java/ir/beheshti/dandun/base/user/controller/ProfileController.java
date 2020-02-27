package ir.beheshti.dandun.base.user.controller;

import ir.beheshti.dandun.base.user.common.BaseOutputDto;
import ir.beheshti.dandun.base.user.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/v1/user/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

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
    public byte[] getUserInfoPhoto() {
        return profileService.getUserInfoPhoto();
    }

}
