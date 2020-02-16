package ir.beheshti.dandun.base.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ir.beheshti.dandun.base.security.SecurityConstants;
import ir.beheshti.dandun.base.user.common.UserException;
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

    public void sendVerificationCodeBySms(SmsInputDto smsInputDto) {
        String verificationCode = verificationCodeService.generateCode();
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
        Optional<UserEntity> userEntityOptional = userRepository.findByPhoneNumber(signUpInputDto.getPhoneNumber());
        if (userEntityOptional.isEmpty()) {
            throw new UserException(1007, "phone number not found");
        } else if (!userEntityOptional.get().isVerified()) {
            throw new UserException(1009, "phone number isn't verified");
        }
        if (userEntityOptional.get().isSignedUp()) {
            throw new UserException(1008, "phone number already signed up");
        }
        userEntityOptional.get().setSignedUp(true);
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
}
