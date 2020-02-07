package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GeneralService {

    @Autowired
    private GeneralService generalService;

    @Autowired
    private UserRepository userRepository;

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public UserEntity getCurrentUserEntity() {
        String username = generalService.getCurrentUsername();
        Optional<UserEntity> userEntityOptional = userRepository.findByPhoneNumber(username);
        if (userEntityOptional.isEmpty()) {
            throw new UserException(5000, "user not found");
        }
        return userEntityOptional.get();
    }

    public Integer getCurrentUserId() {
        return getCurrentUserEntity().getId();
    }
}
