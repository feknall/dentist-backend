package ir.beheshti.dandun.base.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ir.beheshti.dandun.base.security.SecurityConstants;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.login.LoginInputDto;
import ir.beheshti.dandun.base.user.dto.login.LoginOutputDto;
import ir.beheshti.dandun.base.user.dto.signup.SignUpInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsVerificationInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsVerificationOutputDto;
import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.entity.OperatorUserEntity;
import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.DoctorRepository;
import ir.beheshti.dandun.base.user.repository.OperatorRepository;
import ir.beheshti.dandun.base.user.repository.PatientRepository;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import ir.beheshti.dandun.base.user.util.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class LoginAndSignUpService {

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

    @Transactional
    public void sendCode(SmsInputDto smsInputDto) {
        String verificationCode = verificationCodeService.generateCode();
        setVerificationCodeToUserEntity(smsInputDto, verificationCode);
        verificationCodeService.sendSms(smsInputDto.getPhoneNumber(), verificationCode);
    }

    private void setVerificationCodeToUserEntity(SmsInputDto smsInputDto, String verificationCode) {
        Optional<UserEntity> userEntityOptional = userRepository.findByPhoneNumber(smsInputDto.getPhoneNumber());
        if (userEntityOptional.isEmpty()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setPhoneNumber(smsInputDto.getPhoneNumber());
            userEntity.setVerificationCode(verificationCode);
            userRepository.save(userEntity);
        } else {
            userEntityOptional.get().setVerificationCode(verificationCode);
            userRepository.save(userEntityOptional.get());
        }
    }

    @Transactional
    public void signUp(SignUpInputDto signUpInputDto) {

        Optional<UserEntity> userEntityOptional = userRepository.findByPhoneNumber(signUpInputDto.getPhoneNumber());
        if (userEntityOptional.isEmpty()) {
            throw new UserException(1007, "phone number not found");
        } else if (userEntityOptional.get().isValidated()) {
            throw new UserException(1008, "phone number already signed up");
        }
        userEntityOptional.get().setValidated(true);
        userEntityOptional.get().setFirstName(signUpInputDto.getFirstName());
        userEntityOptional.get().setLastName(signUpInputDto.getLastName());

        if (signUpInputDto.getUserType().equals(UserType.Patient)) {
            PatientUserEntity patientUserEntity = new PatientUserEntity();
            patientUserEntity.setId(userEntityOptional.get().getId());
            patientRepository.save(patientUserEntity);
        } else if (signUpInputDto.getUserType().equals(UserType.Doctor)) {
            DoctorUserEntity doctorUserEntity = new DoctorUserEntity();
            doctorUserEntity.setId(userEntityOptional.get().getId());
            doctorRepository.save(doctorUserEntity);
        } else if (signUpInputDto.getUserType().equals(UserType.Operator)) {
            OperatorUserEntity operatorUserEntity = new OperatorUserEntity();
            operatorUserEntity.setId(userEntityOptional.get().getId());
            operatorRepository.save(operatorUserEntity);
        }
    }

    public LoginOutputDto login(LoginInputDto loginInputDto) {
        if (!validationService.isPhoneNumber(loginInputDto.getPhoneNumber())) {
            throw new UserException(1003, "phone number isn't valid");
        }
        Optional<UserEntity> userEntityOptional = userRepository.findByPhoneNumber(loginInputDto.getPhoneNumber());
        if (userEntityOptional.isEmpty()) {
            throw new UserException(1004, "user not found");
        }
        if (userEntityOptional.get().getVerificationCode().equals(loginInputDto.getVerificationCode())) {
            LoginOutputDto loginOutputDto = new LoginOutputDto();
            loginOutputDto.setAccessToken(buildToken(userEntityOptional.get().getPhoneNumber()));
            return loginOutputDto;
        } else {
            throw new UserException(1005, "verification code isn't correct");
        }
    }

//    private UserEntity createUserEntity(SignUpInputDto signUpInputDto) {
//        UserEntity userEntity;
//        if (signUpInputDto.getUserType().equals(UserType.Patient)) {
//            userEntity = new PatientUserEntity();
//        } else if (signUpInputDto.getUserType().equals(UserType.Doctor)) {
//            userEntity = new DoctorUserEntity();
//        } else if (signUpInputDto.getUserType().equals(UserType.Operator)) {
//            userEntity = new OperatorUserEntity();
//        } else
//            throw new UserException(1002, "user type not found");
//        userEntity.setFirstName(signUpInputDto.getFirstName());
//        userEntity.setLastName(signUpInputDto.getLastName());
//        userEntity.setPhoneNumber(signUpInputDto.getPhoneNumber());
//        return userEntity;
//    }

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
        }
        return new SmsVerificationOutputDto(userEntityOptional.get().isValidated(),
                userEntityOptional.get().getVerificationCode().equals(inputDto.getVerificationCode()));
    }
}
