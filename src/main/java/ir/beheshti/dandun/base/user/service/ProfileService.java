package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.user.UserImageOutputDto;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public void updateUserInfoPhoto(MultipartFile file) {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        try {
            userEntity.setProfilePhoto(utilityService.toByteWrapper(file.getBytes()));
            userRepository.save(userEntity);
        } catch (IOException e) {
            throw new UserException(ErrorCodeAndMessage.INTERNAL_SERVER_ERROR_CODE, ErrorCodeAndMessage.INTERNAL_SERVER_ERROR_MESSAGE);
        }
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
