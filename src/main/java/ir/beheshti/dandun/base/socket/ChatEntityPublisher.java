package ir.beheshti.dandun.base.socket;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ChatEntityPublisher {
    private List<Subscriber> subscribers = new ArrayList<>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notifySubscribers(ChatMessageInputDto chatMessageInputDto) {
        for (Subscriber subscriber : subscribers) {
            try {
                subscriber.update(chatMessageInputDto);
            } catch (IOException e) {
                log.error(e);
            }
        }
    }
}
