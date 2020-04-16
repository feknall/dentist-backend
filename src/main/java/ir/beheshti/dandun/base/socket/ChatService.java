package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.beheshti.dandun.base.firebase.PushNotificationRequest;
import ir.beheshti.dandun.base.firebase.PushNotificationService;
import ir.beheshti.dandun.base.user.dto.socket.MessageOutputDto;
import ir.beheshti.dandun.base.user.entity.MessageEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.MessageRepository;
import ir.beheshti.dandun.base.user.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PushNotificationService pushNotificationService;
    @Autowired
    private GeneralService generalService;

    public void sendChatMessage(ChatMessage chatMessage) throws JsonProcessingException {
        MessageEntity messageEntity = chatMessage.toEntity();
        UserEntity userEntity = generalService.getUserEntityById(chatMessage.getToUserId());
        pushNotificationService.doChat(chatMessage, userEntity.getNotificationToken());
        messageRepository.save(messageEntity);
    }

    public List<MessageOutputDto> getUserMessageList(int toUserId) {
        int fromUserId = generalService.getCurrentUserId();
        List<MessageEntity> firstMessageList =
                messageRepository.findAllByFromUserIdAndToUserIdOrderByTimestamp(fromUserId, toUserId);
        List<MessageEntity> secondMessageList =
                messageRepository.findAllByFromUserIdAndToUserIdOrderByTimestamp(toUserId, fromUserId);

        firstMessageList.addAll(secondMessageList);
        return firstMessageList
                .stream()
                .map(MessageOutputDto::fromEntity)
                .collect(Collectors.toList());
    }
}
