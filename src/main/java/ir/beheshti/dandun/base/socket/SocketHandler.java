package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.beheshti.dandun.base.user.service.GeneralService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;

@Service
@Log4j2
public class SocketHandler extends AbstractWebSocketHandler {

    @Autowired
    private GeneralService generalService;

    private final ChatService chatService;

    public SocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        try {
//            if (generalService.getAuthentication(session.getHandshakeHeaders().get(SecurityConstants.HEADER_STRING).get(0)) != null)
//                return;
//        } catch (Exception e) {
//            session.sendMessage(new TextMessage("closing session..."));
//            session.close(CloseStatus.POLICY_VIOLATION);
//        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        SocketResponseDto response = new SocketResponseDto();
        response.setMessage("OK Text");
        try {
            String payload = message.getPayload();
            ChatMessageInputDto chatMessageInputDto = new ObjectMapper().readValue(payload, ChatMessageInputDto.class);
            response.setTimestamp(chatMessageInputDto.getTimestamp());
            chatService.addMessage(chatMessageInputDto);
//            chatService.sendChatMessage(chatMessage);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }

        session.sendMessage(new TextMessage(response.toString()));
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            ChatMessageInputDto chatMessageInputDto = new ChatMessageInputDto();
            chatMessageInputDto.setBinary(message.getPayload().array());
            chatService.addMessage(chatMessageInputDto);
            session.sendMessage(new TextMessage("OK ‌Binary"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
