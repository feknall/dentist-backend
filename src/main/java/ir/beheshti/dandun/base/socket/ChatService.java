package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.beheshti.dandun.base.firebase.PushNotificationService;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.socket.MessageOutputDto;
import ir.beheshti.dandun.base.user.entity.ChatEntity;
import ir.beheshti.dandun.base.user.entity.MessageEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.ChatRepository;
import ir.beheshti.dandun.base.user.repository.DoctorRepository;
import ir.beheshti.dandun.base.user.repository.MessageRepository;
import ir.beheshti.dandun.base.user.service.GeneralService;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import ir.beheshti.dandun.base.user.util.UserType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public void addMessage(ChatMessageInputDto chatMessageInputDto) {
        if (chatMessageInputDto.getMessage() == null || chatMessageInputDto.getMessage().isBlank()) {
            throw new UserException(10000, "message is blank");
        }
        Optional<UserEntity> fromUserEntity = generalService.parseToken(chatMessageInputDto.getToken());
        int chatId;
        Optional<UserEntity> toUserEntity = Optional.empty();
        if (chatMessageInputDto.getChatId() != null) {
            Optional<ChatEntity> chatEntity = chatRepository.findById(chatMessageInputDto.getChatId());
            if (chatEntity.isEmpty()) {
                throw new UserException(10000, "chat entity not found.");
            } else if (fromUserEntity.isEmpty()) {
                throw new UserException(10000, "invalid token");
            } else if (fromUserEntity.get().getUserType() == UserType.Patient) {
                if (chatEntity.get().getPatientId() != fromUserEntity.get().getId()) {
                    throw new UserException(10000, "this patient is not allowed to send message in this chat.");
                }
                toUserEntity = Optional.of(chatEntity.get().getDoctorEntity());
            } else if (fromUserEntity.get().getUserType() == UserType.Doctor) {
                if (chatEntity.get().getDoctorId() != null && chatEntity.get().getDoctorId() != fromUserEntity.get().getId()) {
                    throw new UserException(10000, "this doctor is not allowed to send message in this chat.");
                }
                toUserEntity = Optional.of(chatEntity.get().getPatientEntity());
            }
            chatId = chatEntity.get().getChatId();
        } else {
            ChatEntity chatEntity = new ChatEntity();
            if (fromUserEntity.isEmpty()) {
                throw new UserException(10000, "invalid token");
            } else if (fromUserEntity.get().getUserType() == UserType.Patient) {
                chatEntity.setPatientId(fromUserEntity.get().getId());
            } else if (fromUserEntity.get().getUserType() == UserType.Doctor) {
                throw new UserException(10000, "doctor can't start conversation");
            } else {
                throw new UserException(10000, "unauthorized user");
            }
            chatRepository.save(chatEntity);
            chatId = chatEntity.getChatId();
        }

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setChatId(chatId);
        messageEntity.setChatMessageType(chatMessageInputDto.getChatMessageType());
        messageEntity.setMessage(chatMessageInputDto.getMessage());
        messageEntity.setBinary(chatMessageInputDto.getBinary());
        messageEntity.setTimestamp(chatMessageInputDto.getTimestamp());
        messageEntity.setUserId(fromUserEntity.get().getId());
        messageRepository.save(messageEntity);

        pushNotification(chatMessageInputDto, toUserEntity.orElse(null));
    }

    public void pushNotification(ChatMessageInputDto chatMessageInputDto, UserEntity toUserEntity) {
        List<Pair<Integer, String>> idAndTokenPairSendToList;
        if (toUserEntity == null) {
            idAndTokenPairSendToList = doctorRepository
                    .findAllByDoctorStateType(DoctorStateType.ACTIVE)
                    .stream()
                    .filter(e -> e.getUserEntity().getNotificationToken() != null)
                    .map(e -> Pair.of(e.getDoctorId(), e.getUserEntity().getNotificationToken()))
                    .collect(Collectors.toList());
        } else {
            if (toUserEntity.getNotificationToken() == null) {
                throw new UserException(10000, "user doesn't have token.");
            }
            idAndTokenPairSendToList = Collections.singletonList(Pair.of(toUserEntity.getId(), toUserEntity.getNotificationToken()));
        }
        idAndTokenPairSendToList.forEach(e -> {
            try {
                String data = new ObjectMapper().writeValueAsString(chatMessageInputDto);
                pushNotificationService.doChat(data, e.getSecond());
            } catch (JsonProcessingException jsonProcessingException) {
                log.info(jsonProcessingException);
            }
        });
    }

    public List<MessageOutputDto> getChatMessagesHistory(int chatId) {
        List<MessageEntity> messageEntityList = messageRepository.findAllByChatIdOrderByTimestamp(chatId);
        return messageEntityList
                .stream()
                .map(MessageOutputDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ChatOutputDto> getPatientChatHistory() {
        List<ChatEntity> chatEntityList =
                chatRepository.findAllByPatientId(generalService.getCurrentUserId());
        return chatEntityList
                .stream()
                .map(ChatOutputDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ChatOutputDto> getDoctorChatHistory() {
        List<ChatEntity> chatEntityList =
                chatRepository.findAllByDoctorId(generalService.getCurrentUserId());
        return chatEntityList
                .stream()
                .map(ChatOutputDto::fromEntity).collect(Collectors.toList());
    }
}
