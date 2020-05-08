package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.beheshti.dandun.base.security.SecurityConstants;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.service.GeneralService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.List;

@Log4j2
public class SocketHandler extends AbstractWebSocketHandler {

    private final GeneralService generalService;
    private final ChatService chatService;

    public SocketHandler(ChatService chatService, GeneralService generalService) {
        this.chatService = chatService;
        this.generalService = generalService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            String token = session.getHandshakeHeaders().get(SecurityConstants.HEADER_STRING).get(0);
            if (generalService.getAuthentication(token) != null)
                return;
            UserEntity userEntity = generalService.getByToken(token).get();
            List<Integer> userChatIds = chatService.getUserChatIds(userEntity);
            userChatIds.forEach(id -> chatService.subscribeUser(session, userEntity, id));
        } catch (Exception e) {
            session.sendMessage(new TextMessage("closing session..."));
            session.close(CloseStatus.POLICY_VIOLATION);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        SocketResponseDto response = new SocketResponseDto();
        response.setMessage("OK Text");
        try {
            String payload = message.getPayload();
            ChatMessageInputDto chatMessageInputDto = new ObjectMapper().readValue(payload, ChatMessageInputDto.class);
            String token = session.getHandshakeHeaders().get(SecurityConstants.HEADER_STRING).get(0);
            chatMessageInputDto.setUserId(generalService.getByToken(token).get().getId());
            response.setTimestamp(chatMessageInputDto.getTimestamp());
            chatService.addMessage(chatMessageInputDto);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }

        session.sendMessage(new TextMessage(response.toString()));
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        SocketResponseDto response = new SocketResponseDto();
        response.setMessage("OK Text");
        try {
            ChatMessageInputDto chatMessageInputDto = new ChatMessageInputDto();
            String token = session.getHandshakeHeaders().get(SecurityConstants.HEADER_STRING).get(0);
            chatMessageInputDto.setUserId(generalService.getByToken(token).get().getId());
            chatMessageInputDto.setBinary(message.getPayload().array());
            chatService.addMessage(chatMessageInputDto);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        session.sendMessage(new TextMessage(response.toString()));
    }
}
