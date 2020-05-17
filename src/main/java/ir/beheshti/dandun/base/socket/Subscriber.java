package ir.beheshti.dandun.base.socket;

import ir.beheshti.dandun.base.user.service.UtilityService;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface Subscriber {
    WebSocketSession getSession();
    default void update(SocketResponseDto socketResponseDto) throws IOException {

            getSession().sendMessage(new TextMessage(socketResponseDto.toString()));
//        } else {
//            getSession().sendMessage(new BinaryMessage(UtilityService.fromByteWrapper(socketResponseDto.getChatMessageDto().getBinary())));
//        }
    }
}
