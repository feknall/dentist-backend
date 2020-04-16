package ir.beheshti.dandun.base.firebase;

import com.google.firebase.messaging.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class FCMService {

    public void sendPushNotification(PushNotificationRequest request) {
        try {
            sendMessage(getPayloadData(request.getTitle(), request.getDescription()), request);
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }

    private void sendMessage(Map<String, String> data, PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message;
        if (request.getTopic() != null) {
            message = getPreconfiguredMessageToTopicWithData(data, request);
            log.info("Sent message to topic. topic: {}", request.getTopic());
        } else {
            message = getPreconfiguredMessageToTokenWithData(data, request);
            log.info("Sent message to token. token: {}", request.getToken());
        }
        String response = sendAndGetResponse(message);
        log.info("Sent message. response: {}", response);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setSound(NotificationParameter.SOUND.getValue())
                        .setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build()).build();
    }

    private ApnsConfig getApsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private Message getPreconfiguredMessageToTopicWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request)
                .setTopic(request.getTopic())
                .putAllData(data)
                .build();
    }

    private Message getPreconfiguredMessageToTokenWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request)
                .setToken(request.getToken())
                .putAllData(data)
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApsConfig(request.getTopic());
        return Message.builder()
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
                        new Notification(request.getTitle(), request.getDescription()));
    }

    private Map<String, String> getPayloadData(String title, String description) {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("title", "new title");
        pushData.put("click_action", "FLUTTER_NOTIFICATION_CLICK");
        pushData.put("notification_title", title);
        pushData.put("notification_description", description);
        return pushData;
    }

}