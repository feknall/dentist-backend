package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.user.UserImageInputDto;
import ir.beheshti.dandun.base.user.dto.user.UserImageOutputDto;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final GeneralService generalService;
    private final UtilityService utilityService;

    public ProfileService(UserRepository userRepository, GeneralService generalService, UtilityService utilityService) {
        this.userRepository = userRepository;
        this.generalService = generalService;
        this.utilityService = utilityService;
    }

    public void updateUserInfoPhoto(UserImageInputDto userImageInputDto) {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        userEntity.setProfilePhoto(utilityService.toByteWrapper(userImageInputDto.getImage().getBytes()));
        userRepository.save(userEntity);
    }

    public void removeUserInfoPhoto() {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        userEntity.setProfilePhoto(null);
        userRepository.save(userEntity);
    }

    public UserImageOutputDto getUserInfoImage() {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        if (userEntity.getProfilePhoto() == null) {
            throw new UserException(ErrorCodeAndMessage.USER_DOES_NOT_HAVE_PHOTO_CODE, ErrorCodeAndMessage.USER_DOES_NOT_HAVE_PHOTO_MESSAGE);
        }
        UserImageOutputDto outputDto = new UserImageOutputDto();
        outputDto.setImage(Arrays.toString(userEntity.getProfilePhoto()));
        return outputDto;
    }
}
