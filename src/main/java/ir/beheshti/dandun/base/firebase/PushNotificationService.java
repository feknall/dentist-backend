package ir.beheshti.dandun.base.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.beheshti.dandun.base.socket.ChatMessage;
import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
//    public void sendPushNotificationToTopic(PushNotificationRequest request) {
//        try {
//            fcmService.sendMessageToTopic(getPayloadData(), request);
//        } catch (InterruptedException | ExecutionException e) {
//            log.error(e.getMessage());
//        }
//    }

    public void doChat(ChatMessage chatMessage, String token) throws JsonProcessingException {
        String data = new ObjectMapper().writeValueAsString(chatMessage);
        PushNotificationRequest request = PushNotificationRequest.sendChatToToken(data, token);
    }

    public void sendQuestionToAllDoctors(ChatMessage chatMessage) throws JsonProcessingException {
        String data = new ObjectMapper().writeValueAsString(chatMessage);
        PushNotificationRequest request = PushNotificationRequest.sendChatToTopic(data, TopicType.DOCTOR.getValue());
        fcmService.sendPushNotification(request);
    }

    public void sendChangeDoctorStateNotification(int userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        fcmService.sendPushNotification(buildChangeDoctorStateNotification(userEntity.get().getNotificationToken()));
    }


    public void sendChangePatientStateNotification(int userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.USER_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.USER_NOT_FOUND_MESSAGE);
        }
        fcmService.sendPushNotification(buildChangePatientStateNotification(userEntity.get().getNotificationToken()));
    }

    private PushNotificationRequest buildChangePatientStateNotification(String token) {
        String title = "تغییر وضعیت حساب‌کاربری";
        String description = "بیمار گرامی، وضعیت حساب‌کاربری شما تغییر کرد.";
        return PushNotificationRequest.sendNotificationToToken(title, description, token);
    }

    private PushNotificationRequest buildChangeDoctorStateNotification(String token) {
        String title = "تغییر وضعیت حساب‌کاربری";
        String description = "دندانپزشک گرامی، وضعیت حساب‌کاربری شما تغییر کرد.";
        return PushNotificationRequest.sendNotificationToToken(title, description, token);
    }


}