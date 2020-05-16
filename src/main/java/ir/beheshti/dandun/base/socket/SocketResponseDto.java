package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class SocketResponseDto {
    private long timestamp;
    private boolean ok;
    private boolean show;
    private boolean close = false;
    private String error;
    private ChatMessageInputDto chatMessageDto;

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
