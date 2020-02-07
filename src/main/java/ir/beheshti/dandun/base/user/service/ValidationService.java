package ir.beheshti.dandun.base.user.service;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public boolean isPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() == 12)
            return true;
        return false;
    }
}
