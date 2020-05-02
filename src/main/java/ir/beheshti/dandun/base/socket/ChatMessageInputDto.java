package ir.beheshti.dandun.base.socket;

import lombok.Data;

@Data
public class ChatMessageInputDto {
    private long timestamp;
    private Integer chatId;
    private String message;
    private ChatMessageType chatMessageType;
    private String token;
//    private Integer toUserId;
    private byte[] binary;
}
