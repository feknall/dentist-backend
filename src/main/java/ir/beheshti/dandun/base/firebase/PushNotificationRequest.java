package ir.beheshti.dandun.base.firebase;

import ir.beheshti.dandun.base.socket.ChatOutputDto;
import lombok.Data;

@Data
public class PushNotificationRequest {

    private MessageType messageType;
    private ChatOutputDto chatOutputDto;
    private String title;
    private String description;
    private String topic;
    private String token;

    private PushNotificationRequest() {
    }

    public static PushNotificationRequest sendNotificationToTopic(String title, String description, String topic) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.title = title;
        request.description = description;
        request.topic = topic;
        request.messageType = MessageType.GENERAL;
        return request;
    }

    public static PushNotificationRequest sendChatToToken(String token, ChatOutputDto chatOutputDto) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.title = "شما پیغام جدید دارید";
        request.token = token;
        request.messageType = MessageType.CHAT;
        request.chatOutputDto = chatOutputDto;
        request.description = chatOutputDto.getLastMessage() != null ? chatOutputDto.getLastMessage() : "یک تصویر برای شما ارسال شده است";
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

    public static PushNotificationRequest sendGeneralToToken(String title, String description, String token) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.title = title;
        request.description = description;
        request.token = token;
        request.messageType = MessageType.GENERAL;
        return request;
    }

    public static PushNotificationRequest sendChangeStateToToken(String title, String description, String token) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.title = title;
        request.description = description;
        request.token = token;
        request.messageType = MessageType.USER_STATE;
        return request;
    }

}
