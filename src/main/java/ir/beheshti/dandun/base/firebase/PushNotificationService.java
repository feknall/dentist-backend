package ir.beheshti.dandun.base.firebase;

import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class PushNotificationService {

    @Value("#{${app.notifications.defaults}}")
    private Map<String, String> defaults;

    @Autowired
    private FCMService fcmService;
    @Autowired
    private UserRepository userRepository;

//    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void sendPushNotificationToTopic(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToTopic(getPayloadData(), request);
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }

    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(getPayloadData(), request);
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }

    public void sendChangePatientDoctorNotification(int userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        sendPushNotificationToToken(buildChangeDoctorStateNotification(userEntity.get().getNotificationToken()));
    }


    public void sendChangePatientStateNotification(int userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        sendPushNotificationToToken(buildChangePatientStateNotification(userEntity.get().getNotificationToken()));
    }

    private PushNotificationRequest buildChangePatientStateNotification(String token) {
        String title = "یه تیتر خیلی خفن برای بیمار";
        String description = "من توضیحات خفن‌ترین نوتفیکیشن جهانم. برای اطلاعات بیشتر، کلیک کن!";
        return PushNotificationRequest.toToken(title, description, token);
    }

    private PushNotificationRequest buildChangeDoctorStateNotification(String token) {
        String title = "یه تیتر خیلی خفن برای دکتر";
        String description = "من توضیحات خفن‌ترین نوتفیکیشن جهانم. برای اطلاعات بیشتر، کلیک کن!";
        return PushNotificationRequest.toToken(title, description, token);
    }

    private Map<String, String> getPayloadData() {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("title", "new title");
        pushData.put("click_action", "FLUTTER_NOTIFICATION_CLICK");
        return pushData;
    }
}