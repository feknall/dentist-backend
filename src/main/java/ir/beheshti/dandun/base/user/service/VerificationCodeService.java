package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationCodeService {

    private Random random = new Random();
    @Autowired
    private UserRepository userRepository;

    public void send(UserEntity userEntity) {
        String code = generateCode();
        if (sendSms(userEntity.getPhoneNumber())) {
            userEntity.setVerificationCode(code);
            userRepository.save(userEntity);
            return;
        }
        throw new UserException(2000, "problem in sending sms");
    }

    private String generateCode() {
        int number = random.nextInt(9000);
        return String.valueOf(number + 1000);
    }

    private boolean sendSms(String phoneNumber) {
        return true;
    }
}
