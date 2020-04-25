package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.beheshti.dandun.base.user.common.UserException;
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
        String payload = message.getPayload();
        ChatMessage chatMessage = new ObjectMapper().readValue(payload, ChatMessage.class);

        SocketResponseDto response = new SocketResponseDto();
        response.setMessage("OK");
        try {
            chatService.sendChatMessage(chatMessage);
        } catch (UserException e) {
            response.setMessage(e.getMessage());
        }
        response.setTimestamp(chatMessage.getTimestamp());
        session.sendMessage(new TextMessage(response.toString()));
    }

}
