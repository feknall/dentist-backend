package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import ir.beheshti.dandun.base.user.util.QuestionOwnerType;
import ir.beheshti.dandun.base.user.util.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GeneralService {

    private final UserRepository userRepository;

    public GeneralService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public UserEntity getCurrentUserEntity() {
        String username = getCurrentUsername();
        Optional<UserEntity> userEntityOptional = userRepository.findByPhoneNumber(username);
        if (userEntityOptional.isEmpty()) {
            throw new UserException(5000, "user not found");
        }
        return userEntityOptional.get();
    }

    public int getCurrentUserId() {
        return getCurrentUserEntity().getId();
    }

    public Optional<QuestionOwnerType> getQuestionOwnerTypeFromCurrentUser() {
        UserEntity entity = getCurrentUserEntity();
        if (entity.getUserType().equals(UserType.Doctor)) {
            return Optional.of(QuestionOwnerType.Doctor);
        } else if (entity.getUserType().equals(UserType.Patient)) {
            return Optional.of(QuestionOwnerType.Patient);
        } else
            return Optional.empty();
    }
}
