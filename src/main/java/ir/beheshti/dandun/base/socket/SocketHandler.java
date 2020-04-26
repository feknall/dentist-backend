package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Service
@Log4j2
public class SocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;

    public SocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        log.debug("attribute: {}", session.getAttributes());
        log.debug("principal: {}", session.getPrincipal());
        SocketResponseDto response = new SocketResponseDto();
        response.setMessage("OK");
        try {
            String payload = message.getPayload();
            ChatMessage chatMessage = new ObjectMapper().readValue(payload, ChatMessage.class);
            response.setTimestamp(chatMessage.getTimestamp());
            chatService.sendChatMessage(chatMessage);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }

        session.sendMessage(new TextMessage(response.toString()));
    }

}
