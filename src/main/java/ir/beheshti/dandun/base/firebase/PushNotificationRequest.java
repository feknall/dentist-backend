package ir.beheshti.dandun.base.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PushNotificationRequest {

    public PushNotificationRequest(String title, String body, String topic) {
        this.title = title;
        this.body = body;
        this.topic = topic;
    }

    private String title;
    private String topic;
    private String token;
    private String body;
}
