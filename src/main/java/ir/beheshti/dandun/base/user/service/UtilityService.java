package ir.beheshti.dandun.base.user.service;

import org.springframework.stereotype.Service;

@Service
public class UtilityService {

    public Byte[] toByteWrapper(byte[] bytes) {
        Byte[] myBytes = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            myBytes[i] = bytes[i];
        }
        return myBytes;
    }

    public byte[] fromByteWrapper(Byte[] bytes) {
        byte[] myBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            myBytes[i] = bytes[i];
        }
        return myBytes;
    }
}
