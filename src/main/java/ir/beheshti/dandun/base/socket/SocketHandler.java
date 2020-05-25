package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.beheshti.dandun.base.security.SecurityConstants;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.service.GeneralService;
import ir.beheshti.dandun.base.user.service.UtilityService;
import lombok.extern.log4j.Log4j2;
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
    private final UtilityService utilityService;

    public SocketHandler(ChatService chatService, GeneralService generalService, UtilityService utilityService) {
        this.chatService = chatService;
        this.generalService = generalService;
        this.utilityService = utilityService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            String token = session.getHandshakeHeaders().get(SecurityConstants.HEADER_STRING).get(0);
            if (generalService.getAuthentication(token) != null) {
                UserEntity userEntity = generalService.getByToken(token).get();
                List<Integer> userChatIds = chatService.getUserChatIds(userEntity);
                userChatIds.forEach(id -> chatService.subscribeUser(session, userEntity, id));
                chatService.addOnlineUser(userEntity, session);
            } else {
                throw new UserException(1000, "token not found");
            }
        } catch (Exception e) {
            log.error("Closing session", e);
            session.sendMessage(new TextMessage(e.getMessage()));
            session.sendMessage(new TextMessage("closing session..."));
            session.close(CloseStatus.POLICY_VIOLATION);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String token = session.getHandshakeHeaders().get(SecurityConstants.HEADER_STRING).get(0);
        if (generalService.getAuthentication(token) != null) {
            UserEntity userEntity = generalService.getByToken(token).get();
            List<Integer> userChatIds = chatService.getUserChatIds(userEntity);
            userChatIds.forEach(id -> chatService.unsubscribeUser(userEntity, id));
            chatService.removeOnlineUser(userEntity);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        SocketResponseDto response = new SocketResponseDto();
        response.setOk(true);
        response.setShow(false);
        response.setClose(false);
        Integer chatId = null;
        try {
            String payload = message.getPayload();
            ChatMessageInputDto chatMessageInputDto = new ObjectMapper().readValue(payload, ChatMessageInputDto.class);
            String token = session.getHandshakeHeaders().get(SecurityConstants.HEADER_STRING).get(0);
            chatMessageInputDto.setUserId(generalService.getByToken(token).get().getId());
            response.setTimestamp(chatMessageInputDto.getTimestamp());
            chatId = chatService.addMessage(session, chatMessageInputDto);
        } catch (UserException e) {
            log.error("User exception", e);
            response.setError(e.getMessage());
            response.setOk(false);
            if (e.getCode() == ChatService.CHAT_IS_CLOSED_CODE) {
                response.setClose(true);
            }
        } catch (Exception e) {
            log.error("Unknown exception", e);
            response.setError(e.getMessage());
            response.setOk(false);
        }

        ChatMessageInputDto dto = new ChatMessageInputDto();
        dto.setChatId(chatId);
        response.setChatMessageDto(dto);
        session.sendMessage(new TextMessage(response.toString()));
    }

//    @Override
//    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
//        SocketResponseDto response = new SocketResponseDto();
//        response.setOk(true);
//        response.setShow(false);
//        try {
//            ChatMessageInputDto chatMessageInputDto = new ChatMessageInputDto();
//            String token = session.getHandshakeHeaders().get(SecurityConstants.HEADER_STRING).get(0);
//            chatMessageInputDto.setUserId(generalService.getByToken(token).get().getId());
//            chatMessageInputDto.setChatMessageType(ChatMessageType.IMAGE);
//            chatMessageInputDto.setBinary(utilityService.toByteWrapper(message.getPayload().array()));
//            chatService.addMessage(session, chatMessageInputDto);
//        } catch (Exception e) {
//            log.error("Socket exception", e);
//            response.setError(e.getMessage());
//            response.setOk(false);
//        }
//        session.sendMessage(new TextMessage(response.toString()));
//    }
}
