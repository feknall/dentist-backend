package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;

@Service
@Log4j2
public class SocketHandler extends AbstractWebSocketHandler {

    private final ChatService chatService;

    public SocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("huraaaaaaaai! connection estabilished"));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        SocketResponseDto response = new SocketResponseDto();
        response.setMessage("OK");
        try {
            String payload = message.getPayload();
            ChatMessage chatMessage = new ObjectMapper().readValue(payload, ChatMessage.class);
            response.setTimestamp(chatMessage.getTimestamp());
            chatService.addMessage(chatMessage);
//            chatService.sendChatMessage(chatMessage);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }

        session.sendMessage(new TextMessage(response.toString()));
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setBinary(message.getPayload().array());
            chatService.addMessage(chatMessage);
            session.sendMessage(new TextMessage("OK"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
