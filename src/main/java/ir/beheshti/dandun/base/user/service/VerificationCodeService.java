package ir.beheshti.dandun.base.user.service;

import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationCodeService {

    private Random random = new Random();
    @Autowired
    private UserRepository userRepository;


    public String generateCode() {
        int number = random.nextInt(9000);
        return String.valueOf(number + 1000);
    }

    public void sendSms(String phoneNumber, String code) {
        return;
    }
}
