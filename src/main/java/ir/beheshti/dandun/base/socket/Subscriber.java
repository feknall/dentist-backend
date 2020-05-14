package ir.beheshti.dandun.base.socket;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface Subscriber {
    WebSocketSession getSession();
    default void update(SocketResponseDto socketResponseDto) throws IOException {
        if (socketResponseDto.getChatMessageDto().getMessage() != null) {
            getSession().sendMessage(new TextMessage(socketResponseDto.toString()));
        } else {
            getSession().sendMessage(new BinaryMessage(socketResponseDto.getChatMessageDto().getBinary()));
        }
    }
}
