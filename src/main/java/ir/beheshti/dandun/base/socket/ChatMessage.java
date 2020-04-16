package ir.beheshti.dandun.base.socket;

import ir.beheshti.dandun.base.user.entity.MessageEntity;
import lombok.Data;

@Data
public class ChatMessage {
    private long timestamp;
    private String message;
    private ChatMessageType chatMessageType;
    private Integer fromUserId;
    private Integer toUserId;

    public MessageEntity toEntity() {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setTimestamp(this.getTimestamp());
        messageEntity.setFromUserId(this.getFromUserId());
        messageEntity.setToUserId(this.getToUserId());
        messageEntity.setChatMessageType(this.getChatMessageType());
        return messageEntity;
    }
}
