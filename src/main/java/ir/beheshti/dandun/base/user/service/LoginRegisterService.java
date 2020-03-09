package ir.beheshti.dandun.base.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ir.beheshti.dandun.base.security.SecurityConstants;
import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.operator.OperatorLoginInputDto;
import ir.beheshti.dandun.base.user.dto.operator.OperatorLoginOutputDto;
import ir.beheshti.dandun.base.user.dto.signup.SignUpInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsVerificationInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsVerificationOutputDto;
import ir.beheshti.dandun.base.user.dto.user.UserInfoInputDto;
import ir.beheshti.dandun.base.user.dto.user.UserInfoOutputDto;
import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.entity.OperatorUserEntity;
import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.DoctorRepository;
import ir.beheshti.dandun.base.user.repository.OperatorRepository;
import ir.beheshti.dandun.base.user.repository.PatientRepository;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import ir.beheshti.dandun.base.user.util.UserType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class LoginRegisterService {

    @Autowired
    private ValidationService validationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UtilityService utilityService;

    public void sendVerificationCodeBySms(SmsInputDto smsInputDto) {
        String verificationCode = verificationCodeService.generateCode();
        // todo remove this line of code after getting sms panel
        verificationCode = "1234";
        setVerificationCodeToUserEntity(smsInputDto, verificationCode);
        verificationCodeService.sendSms(smsInputDto.getPhoneNumber(), verificationCode);
    }

    private void setVerificationCodeToUserEntity(SmsInputDto smsInputDto, String verificationCode) {
        Optional<UserEntity> userEntityOptional = userRepository.findByPhoneNumber(smsInputDto.getPhoneNumber());
        if (userEntityOptional.isEmpty()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setVerified(false);
            userEntity.setPhoneNumber(smsInputDto.getPhoneNumber());
            userEntity.setVerificationCode(verificationCode);
            userRepository.save(userEntity);
        } else {
            userEntityOptional.get().setVerificationCode(verificationCode);
            userEntityOptional.get().setVerified(false);
            userRepository.save(userEntityOptional.get());
        }
    }

    public void signUp(SignUpInputDto signUpInputDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByPhoneNumber(generalService.getCurrentUsername());
        if (userEntityOptional.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.PHONE_NUMBER_NOT_FOUND_CODE, ErrorCodeAndMessage.PHONE_NUMBER_NOT_FOUND_MESSAGE);
        } else if (!userEntityOptional.get().isVerified()) {
            throw new UserException(ErrorCodeAndMessage.PHONE_NUMBER_IS_NOT_VERIFIED_CODE,
                    ErrorCodeAndMessage.PHONE_NUMBER_IS_NOT_VERIFIED_MESSAGE);
        } else if (userEntityOptional.get().isSignedUp()) {
            throw new UserException(ErrorCodeAndMessage.PHONE_NUMBER_ALREADY_SINGED_UP_CODE,
                    ErrorCodeAndMessage.PHONE_NUMBER_ALREADY_SINGED_UP_MESSAGE);
        }

        userEntityOptional.get().setSignedUp(true);
        userEntityOptional.get().setFirstName(signUpInputDto.getFirstName());
        userEntityOptional.get().setLastName(signUpInputDto.getLastName());

        if (signUpInputDto.getUserType().equals(UserType.Patient)) {
            userEntityOptional.get().setUserType(UserType.Patient);

            PatientUserEntity patientUserEntity = new PatientUserEntity();
            patientUserEntity.setPatientId(userEntityOptional.get().getId());
            patientUserEntity.setPatientStateType(PatientStateType.NOT_ANSWERED);
            patientRepository.save(patientUserEntity);
        } else if (signUpInputDto.getUserType().equals(UserType.Doctor)) {
            userEntityOptional.get().setUserType(UserType.Doctor);

            DoctorUserEntity doctorUserEntity = new DoctorUserEntity();
            doctorUserEntity.setDoctorId(userEntityOptional.get().getId());
            doctorUserEntity.setDoctorStateType(DoctorStateType.NOT_ANSWERED);
            doctorRepository.save(doctorUserEntity);
        } else if (signUpInputDto.getUserType().equals(UserType.Operator)) {
            userEntityOptional.get().setUserType(UserType.Operator);

            OperatorUserEntity operatorUserEntity = new OperatorUserEntity();
            operatorUserEntity.setOperatorId(userEntityOptional.get().getId());
            operatorRepository.save(operatorUserEntity);
        }
        userRepository.save(userEntityOptional.get());
    }

    private String buildToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }

    public SmsVerificationOutputDto verifyPhoneNumber(SmsVerificationInputDto inputDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByPhoneNumber(inputDto.getPhoneNumber());
        if (userEntityOptional.isEmpty()) {
            throw new UserException(1006, "phoneNumber not found");
        } else if (userEntityOptional.get().isVerified()) {
            throw new UserException(1010, "user already verified");
        }
        boolean isVerified = userEntityOptional.get().getVerificationCode().equals(inputDto.getVerificationCode());
        if (!isVerified) {
            return new SmsVerificationOutputDto(null, false, null);
        }
        boolean isNewUser = !userEntityOptional.get().isSignedUp();
        userEntityOptional.get().setVerified(true);
        userEntityOptional.get().setVerificationCode(null);
        userRepository.save(userEntityOptional.get());
        return new SmsVerificationOutputDto(isNewUser, true, buildToken(userEntityOptional.get().getPhoneNumber()));
    }

    public UserInfoOutputDto getUserInfo() {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        return UserInfoOutputDto.fromEntity(userEntity);
    }

    public void updateUserInfo(UserInfoInputDto input) {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        BeanUtils.copyProperties(input, userEntity);
        userRepository.save(userEntity);
    }

    public OperatorLoginOutputDto loginOperator(OperatorLoginInputDto operatorLoginInputDto) {
        Optional<UserEntity> userEntity = userRepository.findByPhoneNumber(operatorLoginInputDto.getUsername());
        if (userEntity.isEmpty() || userEntity.get().getUserType() != UserType.Operator) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE, ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        } else if (!operatorLoginInputDto.getPassword().equals(userEntity.get().getPassword())) {
            throw new UserException(1, "wrong password");
        }
        OperatorLoginOutputDto dto = new OperatorLoginOutputDto();
        dto.setToken(buildToken(operatorLoginInputDto.getUsername()));
        return dto;
    }
}
