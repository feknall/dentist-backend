package ir.beheshti.dandun.base.user.dto.socket;

import ir.beheshti.dandun.base.socket.ChatMessageType;
import ir.beheshti.dandun.base.user.entity.MessageEntity;
import lombok.Data;

@Data
public class MessageOutputDto {
    private long timestamp;
    private String message;
    private ChatMessageType chatMessageType;
    private boolean isBinary;
    private Integer userId;

    public static MessageOutputDto fromEntity(MessageEntity messageEntity) {
        MessageOutputDto dto = new MessageOutputDto();
        dto.timestamp = messageEntity.getTimestamp();
        dto.message = messageEntity.getMessage();
        dto.chatMessageType = messageEntity.getChatMessageType();
        dto.isBinary = messageEntity.getBinary() == null ? false : true;
        dto.userId = messageEntity.getUserId();
        return dto;
    }
}
