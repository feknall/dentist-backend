package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.notification.NotificationRepository;
import ir.beheshti.dandun.base.user.dto.user.UserImageInputDto;
import ir.beheshti.dandun.base.user.dto.user.UserImageOutputDto;
import ir.beheshti.dandun.base.user.dto.user.UserNotificationInputDto;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserNotificationRepository;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UserNotificationRepository userNotificationRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    public void updateUserInfoPhoto(UserImageInputDto userImageInputDto) {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        userEntity.setProfilePhoto(userImageInputDto.getImage());
        userRepository.save(userEntity);
    }

    public void removeUserInfoPhoto() {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        userEntity.setProfilePhoto(null);
        userRepository.save(userEntity);
    }

    public UserImageOutputDto getUserInfoImage() {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        UserImageOutputDto outputDto = new UserImageOutputDto();
        outputDto.setImage(userEntity.getProfilePhoto());
        return outputDto;
    }

    public void setSetUserNotificationToken(UserNotificationInputDto userNotificationInputDto) {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        userEntity.setNotificationToken(userNotificationInputDto.getNotificationToken());
        userRepository.save(userEntity);
    }

    public void logout() {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        userEntity.setNotificationToken("");
        userRepository.save(userEntity);
    }
}
