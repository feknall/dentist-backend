package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.beheshti.dandun.base.firebase.PushNotificationService;
import ir.beheshti.dandun.base.user.dto.socket.MessageOutputDto;
import ir.beheshti.dandun.base.user.entity.ChatEntity;
import ir.beheshti.dandun.base.user.entity.MessageEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.ChatRepository;
import ir.beheshti.dandun.base.user.repository.DoctorRepository;
import ir.beheshti.dandun.base.user.repository.MessageRepository;
import ir.beheshti.dandun.base.user.service.GeneralService;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ChatService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PushNotificationService pushNotificationService;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private ChatRepository chatRepository;

    public void sendChatMessage(ChatMessage chatMessage) {
        List<Pair<Integer, String>> sentTo;
        if (chatMessage.getToUserId() == null) {
            sentTo = doctorRepository
                    .findAllByDoctorStateType(DoctorStateType.ACTIVE)
                    .stream()
                    .map(e -> Pair.of(e.getDoctorId(), e.getUserEntity().getNotificationToken()))
                    .collect(Collectors.toList());
        } else {
            UserEntity userEntity = generalService.getUserEntityById(chatMessage.getToUserId());
            sentTo = Collections.singletonList(Pair.of(userEntity.getId(), userEntity.getNotificationToken()));
        }
        sentTo.forEach(e -> {
            try {
                String data = new ObjectMapper().writeValueAsString(chatMessage);
                pushNotificationService.doChat(data, e.getSecond());
            } catch (JsonProcessingException jsonProcessingException) {
                log.info(jsonProcessingException);
            }

            ChatEntity chatEntity = new ChatEntity();
            UserEntity userEntity = generalService.parseToken(chatMessage.getToken()).get();
            chatEntity.setPatientId(userEntity.getId());
            chatEntity.setDoctorId(e.getFirst());
            chatRepository.save(chatEntity);

            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setChatId(chatEntity.getChatId());
            messageEntity.setMessage(chatMessage.getMessage());
            messageEntity.setChatMessageType(chatMessage.getChatMessageType());
            messageEntity.setTimestamp(chatMessage.getTimestamp());
            messageEntity.setUserId(userEntity.getId());
            messageRepository.save(messageEntity);

            chatEntity.setMessageId(messageEntity.getMessageId());
            chatRepository.save(chatEntity);
        });
    }

    public List<MessageOutputDto> getChatMessages(int chatId) {
        List<MessageEntity> messageEntityList = messageRepository.findAllByChatIdOrderByTimestamp(chatId);
        return messageEntityList
                .stream()
                .map(MessageOutputDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ChatOutputDto> getPatientChat() {
        List<ChatEntity> outputList = new ArrayList<>();
        List<ChatEntity> chatEntityList =
                chatRepository.findAllByPatientId(generalService.getCurrentUserId());
        chatEntityList.forEach(chatEntity -> {
            outputList.forEach(output -> {
                if (!chatEntity.getMessageId().equals(output.getMessageId())) {
                    outputList.add(chatEntity);
                }
            });
        });
        return outputList
                .stream()
                .map(ChatOutputDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ChatOutputDto> getDoctorChat() {
        List<ChatEntity> chatEntityList =
                chatRepository.findAllByDoctorId(generalService.getCurrentUserId());
        return chatEntityList
                .stream()
                .map(ChatOutputDto::fromEntity).collect(Collectors.toList());
    }
}
