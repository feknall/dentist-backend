package ir.beheshti.dandun.base.socket;

import ir.beheshti.dandun.base.user.entity.MessageEntity;
import lombok.Data;

@Data
public class ChatMessage {
    private long timestamp;
    private String message;
    private ChatMessageType chatMessageType;
    private String token;
    private Integer toUserId;
}
