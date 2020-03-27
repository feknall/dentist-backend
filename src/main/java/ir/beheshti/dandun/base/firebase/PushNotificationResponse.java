package ir.beheshti.dandun.base.firebase;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PushNotificationResponse {
    private int status;
    private String message;
}
