package ir.beheshti.dandun.base.firebase;

import ir.beheshti.dandun.base.socket.ChatOutputDto;
import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
public class PushNotificationService {

    @Value("#{${app.notifications.defaults}}")
    private Map<String, String> defaults;

    @Autowired
    private FCMService fcmService;
    @Autowired
    private UserRepository userRepository;

    public void doChat(String token, ChatOutputDto chatOutputDto) {
        PushNotificationRequest request = PushNotificationRequest.sendChatToToken(token, chatOutputDto);
        fcmService.sendPushNotification(request);
    }

    public void sendChangeDoctorStateNotification(String token, String newState) {
        try {
            String title = "تغییر وضعیت حساب‌کاربری";
            String description = "دندانپزشک گرامی، وضعیت حساب‌کاربری شما تغییر کرد.";
            PushNotificationRequest request = PushNotificationRequest.sendChangeStateToToken(title, description, token);
            fcmService.sendPushNotification(request);
        } catch (Exception e) {
            log.debug("Change doctor state notification problem, Error during push notification", e);
        }
    }

    public void sendChangePatientStateNotification(String token) {
        try {
            String title = "تغییر وضعیت حساب‌کاربری";
            String description = "بیمار گرامی، وضعیت حساب‌کاربری شما تغییر کرد.";
            PushNotificationRequest request = PushNotificationRequest.sendChangeStateToToken(title, description, token);
            fcmService.sendPushNotification(request);
        } catch (Exception e) {
            log.debug("Change patient state notification problem, Error during push notification", e);
        }
    }

    public void sendScheduledNotification(PushNotificationRequest pushNotificationRequest) {
        try {
            fcmService.sendPushNotification(pushNotificationRequest);
        } catch (Exception e) {
            log.debug("Problem when sending notification", e);
        }
    }
}