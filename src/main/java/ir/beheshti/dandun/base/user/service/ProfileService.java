package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.dto.notification.NotificationOutputDto;
import ir.beheshti.dandun.base.user.dto.user.UserImageInputDto;
import ir.beheshti.dandun.base.user.dto.user.UserImageOutputDto;
import ir.beheshti.dandun.base.user.dto.user.UserNotificationInputDto;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserNotificationRepository;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UserNotificationRepository userNotificationRepository;

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

    public List<NotificationOutputDto> getUserNotificationList() {
       return userNotificationRepository
               .findAllByUserId(generalService.getCurrentUserId())
               .stream()
               .map(e -> NotificationOutputDto.fromEntity(e.getNotificationEntity()))
               .collect(Collectors.toList());
    }
}
