package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UtilityService utilityService;

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

    public byte[] getUserInfoPhoto() {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        if (userEntity.getProfilePhoto() == null) {
            throw new UserException(ErrorCodeAndMessage.USER_DOES_NOT_HAVE_PHOTO_CODE, ErrorCodeAndMessage.USER_DOES_NOT_HAVE_PHOTO_MESSAGE);
        }
        return utilityService.fromByteWrapper(userEntity.getProfilePhoto());
    }
}
