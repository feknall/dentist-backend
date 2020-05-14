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
            } catch (IOException e) {
                log.error(e);
            }
        }
    }
}
