package ir.beheshti.dandun.base.firebase;

import lombok.Data;

@Data
public class PushNotificationRequest {

    private MessageType messageType;
    private String title;
    private String description;
    private String topic;
    private String token;

    private PushNotificationRequest() {}

    public static PushNotificationRequest sendNotificationToTopic(String title, String description, String topic) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.title = title;
        request.description = description;
        request.topic = topic;
        request.messageType = MessageType.NOTIFICATION;
        return request;
    }

    public static PushNotificationRequest sendChatToToken(String description, String token) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.title = "شما پیغام جدید دارید";
        request.description = description;
        request.token = token;
        request.messageType = MessageType.CHAT;
        return request;
    }

    public static PushNotificationRequest sendChatToTopic(String description, String topic) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.title = "شما پیغام جدید دارید";
        request.description = description;
        request.topic = topic;
        request.messageType = MessageType.CHAT;
        return request;
    }

    public static PushNotificationRequest sendNotificationToToken(String title, String description, String token) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.title = title;
        request.description = description;
        request.token = token;
        request.messageType = MessageType.NOTIFICATION;
        return request;
    }

}
