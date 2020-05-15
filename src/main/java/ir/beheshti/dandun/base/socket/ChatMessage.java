package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
public class ChatMessage {
    private long timestamp;
    private Integer chatId;
    private Integer fromUserId;
    private Integer toUserId;
    private String message;
    private ChatMessageType chatMessageType;
    private Byte[] binary;

    public static ChatMessage fromChatMessageInputDto(ChatMessageInputDto dto, Integer fromUserId, Integer toUserId) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.timestamp = dto.getTimestamp();
        chatMessage.chatId = dto.getChatId();
        chatMessage.fromUserId = fromUserId;
        chatMessage.toUserId = toUserId;
        chatMessage.message = dto.getMessage();
        chatMessage.chatMessageType = dto.getChatMessageType();
        chatMessage.binary = dto.getBinary();
        return chatMessage;
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(e);
            return null;
        }
    }
}
