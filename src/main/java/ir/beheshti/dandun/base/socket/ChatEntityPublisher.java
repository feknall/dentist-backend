package ir.beheshti.dandun.base.socket;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ChatEntityPublisher {
    private Map<Integer, Subscriber> userIdSubscriberMap = new HashMap<>();

    public void addSubscriber(int userId, Subscriber subscriber) {
        userIdSubscriberMap.put(userId, subscriber);
    }

    public void removeSubscriber(int userId) {
        userIdSubscriberMap.remove(userId);
    }

    public void notifySubscribers(int userId, SocketResponseDto socketResponseDto) {
        for (Map.Entry<Integer, Subscriber> entry : userIdSubscriberMap.entrySet()) {
            try {
                if (entry.getKey() != userId)
                    entry.getValue().update(socketResponseDto);
                else {
                    SocketResponseDto dto = new SocketResponseDto();
                    dto.setTimestamp(System.currentTimeMillis());
                    dto.setShow(false);
                    dto.setOk(true);
                    ChatMessageInputDto messageInputDto = new ChatMessageInputDto();
                    messageInputDto.setChatId(socketResponseDto.getChatMessageDto().getChatId());
                    dto.setChatMessageDto(messageInputDto);
                    entry.getValue().update(dto);
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
    }
}
