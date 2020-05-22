package ir.beheshti.dandun.base.socket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface Subscriber {
    WebSocketSession getSession();

    default void update(SocketResponseDto socketResponseDto) throws IOException {
        if (getSession().isOpen()) {
            getSession().sendMessage(new TextMessage(socketResponseDto.toString()));
        }
    }
}
