package ir.beheshti.dandun.base.socket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatKey {
    private Integer chatId;
    private String token;
    private Integer toUserId;

    public String getKey() {
        return chatId + "---" + token + "---" + toUserId;
    }
}
