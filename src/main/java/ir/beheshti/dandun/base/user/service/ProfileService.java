package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.dto.user.UserImageInputDto;
import ir.beheshti.dandun.base.user.dto.user.UserImageOutputDto;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final GeneralService generalService;

    public ProfileService(UserRepository userRepository, GeneralService generalService) {
        this.userRepository = userRepository;
        this.generalService = generalService;
    }

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
}
