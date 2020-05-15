package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
public class ChatMessageInputDto {
    private long timestamp;
    private Integer chatId;
    private String message;
    private ChatMessageType chatMessageType;
    private Byte[] binary;
    private Integer userId;

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(e);
            return null;
        }
    }
}
