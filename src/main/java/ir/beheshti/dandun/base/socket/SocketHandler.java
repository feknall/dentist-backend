package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Log4j2
public class SocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatService chatService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        log.debug("attribute: {}", session.getAttributes());
        log.debug("principal: {}", session.getPrincipal());
        String payload = message.getPayload();
        ChatMessage chatMessage = new ObjectMapper().readValue(payload, ChatMessage.class);
        chatService.sendChatMessage(chatMessage);
        session.sendMessage(new TextMessage(String.valueOf(chatMessage.getTimestamp())));
    }

}
