package ir.beheshti.dandun.base.user.dto.socket;

import ir.beheshti.dandun.base.socket.ChatMessageType;
import ir.beheshti.dandun.base.user.entity.MessageEntity;
import lombok.Data;

@Data
public class MessageOutputDto {
    private long timestamp;
    private String message;
    private ChatMessageType chatMessageType;
    private Integer fromUserId;
    private Integer toUserId;

    public static MessageOutputDto fromEntity(MessageEntity messageEntity) {
        MessageOutputDto dto = new MessageOutputDto();
        dto.timestamp = messageEntity.getTimestamp();
        dto.message = messageEntity.getMessage();
        dto.chatMessageType = messageEntity.getChatMessageType();
        dto.fromUserId = messageEntity.getFromUserId();
        dto.toUserId = messageEntity.getToUserId();
        return dto;
    }
}
