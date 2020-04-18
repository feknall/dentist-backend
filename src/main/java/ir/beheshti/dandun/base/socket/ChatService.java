package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.beheshti.dandun.base.firebase.PushNotificationRequest;
import ir.beheshti.dandun.base.firebase.PushNotificationService;
import ir.beheshti.dandun.base.user.dto.socket.MessageOutputDto;
import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.entity.MessageEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.entity.UserMessageEntity;
import ir.beheshti.dandun.base.user.repository.DoctorRepository;
import ir.beheshti.dandun.base.user.repository.MessageRepository;
import ir.beheshti.dandun.base.user.repository.UserMessageRepository;
import ir.beheshti.dandun.base.user.service.GeneralService;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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
    @Autowired
    private UserMessageRepository userMessageRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    public void sendChatMessage(ChatMessage chatMessage) throws JsonProcessingException {
        MessageEntity messageEntity = chatMessage.toEntity();
        if (chatMessage.getToUserId() == null) {
            pushNotificationService.sendQuestionToAllDoctors(chatMessage);
            messageRepository.save(messageEntity);
            List<DoctorUserEntity> doctorUserEntityList =
                    doctorRepository.findAllByDoctorStateType(DoctorStateType.ACTIVE);
            doctorUserEntityList.forEach(e -> {
                UserMessageEntity userMessageEntity = new UserMessageEntity();
                userMessageEntity.setFromUserId(chatMessage.getFromUserId());
                userMessageEntity.setToUserId(e.getDoctorId());
                userMessageEntity.setMessageId(messageEntity.getMessageId());
                userMessageRepository.save(userMessageEntity);
            });
        } else {
            UserEntity userEntity = generalService.getUserEntityById(chatMessage.getToUserId());
            pushNotificationService.doChat(chatMessage, userEntity.getNotificationToken());
            UserMessageEntity userMessageEntity = new UserMessageEntity();
            userMessageEntity.setFromUserId(chatMessage.getFromUserId());
            userMessageEntity.setToUserId(userEntity.getId());
            userMessageEntity.setMessageId(messageEntity.getMessageId());
            userMessageRepository.save(userMessageEntity);
        }
    }

    public List<MessageOutputDto> getUserMessageList(Integer secondUserId) {
        int firstUserId = generalService.getCurrentUserId();
        List<UserMessageEntity> allMessages;
        List<UserMessageEntity> sent;
        List<UserMessageEntity> received;
        if (secondUserId != null) {
            sent = userMessageRepository.findAllByFromUserIdAndToUserId(firstUserId, secondUserId);
            received = userMessageRepository.findAllByFromUserIdAndToUserId(secondUserId, firstUserId);
        } else {
            sent = userMessageRepository.findAllByFromUserId(firstUserId);
            received = userMessageRepository.findAllByToUserId(secondUserId);
        }
        return Stream
                .of(sent, received)
                .flatMap(Collection::stream)
                .map(MessageOutputDto::fromEntity)
                .collect(Collectors.toList());
    }
}
