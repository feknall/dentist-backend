package ir.beheshti.dandun.base.socket;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface Subscriber {
    WebSocketSession getSession();
    default void update(ChatMessageInputDto chatMessageInputDto) throws IOException {
        if (chatMessageInputDto.getMessage() != null) {
            getSession().sendMessage(new TextMessage(chatMessageInputDto.toJson()));
        } else {
            getSession().sendMessage(new BinaryMessage(chatMessageInputDto.getBinary()));
        }
    }
}
