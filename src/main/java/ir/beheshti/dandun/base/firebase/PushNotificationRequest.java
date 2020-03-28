package ir.beheshti.dandun.base.firebase;

import lombok.Data;

@Data
public class PushNotificationRequest {

    private String title;
    private String description;
    private String topic;
    private String token;

    private PushNotificationRequest() {}

    public static PushNotificationRequest toTopic(String title, String description, String topic) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.title = title;
        request.description = description;
        request.topic = topic;
        return request;
    }

    public static PushNotificationRequest toToken(String title, String description, String token) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.title = title;
        request.description = description;
        request.token = token;
        return request;
    }

}
