package ir.beheshti.dandun.base.socket;

import lombok.Data;

@Data
public class ChatMessage {
    private long timestamp;
    private Integer chatId;
    private Integer fromUserId;
    private Integer toUserId;
    private String message;
    private ChatMessageType chatMessageType;
    private byte[] binary;

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
}
