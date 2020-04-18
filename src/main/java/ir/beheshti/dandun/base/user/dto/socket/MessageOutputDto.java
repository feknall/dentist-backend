package ir.beheshti.dandun.base.user.dto.socket;

import ir.beheshti.dandun.base.socket.ChatMessageType;
import ir.beheshti.dandun.base.user.entity.UserMessageEntity;
import lombok.Data;

@Data
public class MessageOutputDto {
    private long timestamp;
    private String message;
    private ChatMessageType chatMessageType;
    private Integer fromUserId;
    private Integer toUserId;

    public static MessageOutputDto fromEntity(UserMessageEntity userMessageEntity) {
        MessageOutputDto dto = new MessageOutputDto();
        dto.timestamp = userMessageEntity.getMessageEntity().getTimestamp();
        dto.message = userMessageEntity.getMessageEntity().getMessage();
        dto.chatMessageType = userMessageEntity.getMessageEntity().getChatMessageType();
        dto.fromUserId = userMessageEntity.getFromUserId();
        dto.toUserId = userMessageEntity.getToUserId();
        return dto;
    }
}
