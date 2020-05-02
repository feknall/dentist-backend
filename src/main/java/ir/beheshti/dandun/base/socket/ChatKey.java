package ir.beheshti.dandun.base.socket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatKey {
    private Integer chatId;
    private Integer fromUserId;
    private Integer toUserId;

    public static String getKey(ChatMessage chatMessage) {
        return chatMessage.getChatId() + "---" + chatMessage.getFromUserId() + "---" + chatMessage.getToUserId();
    }


}
