package ir.beheshti.dandun.base.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ir.beheshti.dandun.base.security.SecurityConstants;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.login.LoginInputDto;
import ir.beheshti.dandun.base.user.dto.login.LoginOutputDto;
import ir.beheshti.dandun.base.user.dto.signup.SignUpInputDto;
import ir.beheshti.dandun.base.user.dto.sms.SmsInputDto;
import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.entity.OperatorUserEntity;
import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
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
    private VerificationCodeService verificationCodeService;

    @Transactional
    public void sendCode(SmsInputDto loginInputDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByPhoneNumber(loginInputDto.getPhoneNumber());
        if (userEntityOptional.isEmpty()) {
            throw new UserException(1000, "user not found");
        }
        verificationCodeService.send(userEntityOptional.get());
    }

    @Transactional
    public void signUp(SignUpInputDto signUpInputDto) {
        if (!validationService.isPhoneNumber(signUpInputDto.getPhoneNumber())) {
            throw new UserException(1001, "phone number isn't valid");
        }
        verificationCodeService.send(createUserEntity(signUpInputDto));
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

    private UserEntity createUserEntity(SignUpInputDto signUpInputDto) {
        UserEntity userEntity;
        if (signUpInputDto.getUserType().equals(UserType.Patient.getValue())) {
            userEntity = new PatientUserEntity();
        } else if (signUpInputDto.getUserType().equals(UserType.Doctor.getValue())) {
            userEntity = new DoctorUserEntity();
        } else if (signUpInputDto.getUserType().equals(UserType.Operator.getValue())) {
            userEntity = new OperatorUserEntity();
        } else
            throw new UserException(1002, "user type not found");
        userEntity.setFirstName(signUpInputDto.getFirstName());
        userEntity.setLastName(signUpInputDto.getLastName());
        userEntity.setPhoneNumber(signUpInputDto.getPhoneNumber());
        return userEntity;
    }

    public String buildToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }
}
