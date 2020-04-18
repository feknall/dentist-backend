package ir.beheshti.dandun.base.user.service;

import io.jsonwebtoken.Jwts;
import ir.beheshti.dandun.base.security.SecurityConstants;
import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.DoctorRepository;
import ir.beheshti.dandun.base.user.repository.PatientRepository;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import ir.beheshti.dandun.base.user.util.QuestionOwnerType;
import ir.beheshti.dandun.base.user.util.UserType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GeneralService {


    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public GeneralService(DoctorRepository doctorRepository, PatientRepository patientRepository, UserRepository userRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
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

    public PatientUserEntity getCurrentPatient() {
        int userId = getCurrentUserId();
        Optional<PatientUserEntity> patientUserEntityOptional = patientRepository.findById(userId);
        if (patientUserEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        return patientUserEntityOptional.get();
    }

    public DoctorUserEntity getCurrentDoctor() {
        int userId = getCurrentUserId();
        Optional<DoctorUserEntity> doctorUserEntityOptional = doctorRepository.findById(userId);
        if (doctorUserEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        return doctorUserEntityOptional.get();
    }

    public UserEntity getCurrentUserEntity() {
        String username = getCurrentUsername();
        Optional<UserEntity> userEntityOptional = userRepository.findByPhoneNumber(username);
        if (userEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        return userEntityOptional.get();
    }

    public int getCurrentUserId() {
        return getCurrentUserEntity().getId();
    }

    public UserEntity getUserEntityById(int userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        return userEntityOptional.get();
    }

    public void checkUserExistence(int userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
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

    public Optional<UserEntity> parseToken(String token) {
        String user = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();
        if (user == null)
            return Optional.empty();
        return userRepository.findByPhoneNumber(user);
    }
}
