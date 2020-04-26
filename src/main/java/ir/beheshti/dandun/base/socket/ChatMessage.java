package ir.beheshti.dandun.base.socket;

import lombok.Data;

@Data
public class ChatMessage {
    private long timestamp;
    private String message;
    private ChatMessageType chatMessageType;
    private String token;
    private Integer toUserId;
}
