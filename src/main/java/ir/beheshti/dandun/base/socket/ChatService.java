package ir.beheshti.dandun.base.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.beheshti.dandun.base.firebase.PushNotificationService;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.dto.socket.MessageOutputDto;
import ir.beheshti.dandun.base.user.entity.ChatEntity;
import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.entity.MessageEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.ChatRepository;
import ir.beheshti.dandun.base.user.repository.DoctorRepository;
import ir.beheshti.dandun.base.user.repository.MessageRepository;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import ir.beheshti.dandun.base.user.service.GeneralService;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import ir.beheshti.dandun.base.user.util.UserType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ChatService {
    public static final int CHAT_IS_CLOSED_CODE = 19999;
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
    @Autowired
    private UserRepository userRepository;

    private Map<UserEntity, WebSocketSession> onlinePatients = new HashMap<>();
    private Map<UserEntity, WebSocketSession> onlineDoctors = new HashMap<>();

    // chatId - chatEntity
    private Map<Integer, ChatEntityPublisher> chatEntityPublisherMap = new HashMap<>();

    public void addOnlineUser(UserEntity userEntity, WebSocketSession session) {
        if (userEntity.getUserType() == UserType.Doctor) {
            onlineDoctors.put(userEntity, session);
        } else if (userEntity.getUserType() == UserType.Patient) {
            onlinePatients.put(userEntity, session);
        }
    }

    public void removeOnlineUser(UserEntity userEntity) {
        if (userEntity.getUserType() == UserType.Doctor && onlineDoctors.containsKey(userEntity)) {
            onlineDoctors.remove(userEntity);
        } else if (userEntity.getUserType() == UserType.Patient && onlinePatients.containsKey(userEntity)) {
            onlinePatients.remove(userEntity);
        }
    }

    public void unsubscribeUser(UserEntity userEntity, Integer chatId) {
        if (chatEntityPublisherMap.containsKey(chatId)) {
            chatEntityPublisherMap.get(chatId).removeSubscriber(userEntity.getId());
        }
    }

    public void subscribeUser(WebSocketSession session, UserEntity userEntity, Integer chatId) {
        userEntity.setSession(session);
        if (chatEntityPublisherMap.containsKey(chatId)) {
            chatEntityPublisherMap.get(chatId).addSubscriber(userEntity.getId(), userEntity);
        } else {
            ChatEntityPublisher chatEntityPublisher = new ChatEntityPublisher();
            chatEntityPublisher.addSubscriber(userEntity.getId(), userEntity);
            chatEntityPublisherMap.put(chatId, chatEntityPublisher);
        }
    }

    public Integer addMessage(WebSocketSession session, ChatMessageInputDto chatMessageInputDto) {
        if ((chatMessageInputDto.getMessage() == null || chatMessageInputDto.getMessage().isBlank()) &&
                (chatMessageInputDto.getBinary() == null || chatMessageInputDto.getBinary().isBlank())) {
            throw new UserException(10000, "message and binary section are blank.");
        }
        UserEntity fromUserEntity = userRepository
                .findById(chatMessageInputDto.getUserId())
                .orElseThrow(() -> new UserException(10000, "user not found"));
        int chatId;
        Optional<UserEntity> toUserEntity = Optional.empty();
        if (chatMessageInputDto.getChatId() != null) {
            Optional<ChatEntity> chatEntity = chatRepository.findById(chatMessageInputDto.getChatId());
            if (chatEntity.isEmpty()) {
                throw new UserException(10000, "chat entity not found.");
            } else if (chatEntity.get().getChatStateType() == ChatStateType.CLOSED) {
                throw new UserException(CHAT_IS_CLOSED_CODE, "chat is closed");
            } else if (fromUserEntity.getUserType() == UserType.Patient) {
                if (chatEntity.get().getPatientId() != fromUserEntity.getId()) {
                    throw new UserException(10000, "this patient is not allowed to send message in this chat.");
                }
            } else if (fromUserEntity.getUserType() == UserType.Doctor) {
                if (chatEntity.get().getDoctorId() != null && chatEntity.get().getDoctorId() != fromUserEntity.getId()) {
                    throw new UserException(10000, "this doctor is not allowed to send message in this chat.");
                }
                toUserEntity = Optional.of(chatEntity.get().getPatientEntity());
                chatEntity.get().setDoctorId(fromUserEntity.getId());
                chatEntity.get().setChatStateType(ChatStateType.OPEN);
                chatRepository.save(chatEntity.get());
                subscribeUser(session, fromUserEntity, chatEntity.get().getChatId());
            }
            chatId = chatEntity.get().getChatId();
        } else {
            ChatEntity chatEntity = new ChatEntity();
            chatEntity.setChatStateType(ChatStateType.SEARCHING);
            if (fromUserEntity.getUserType() == UserType.Patient) {
                chatEntity.setPatientId(fromUserEntity.getId());
            } else if (fromUserEntity.getUserType() == UserType.Doctor) {
                throw new UserException(10000, "doctor can't start conversation");
            } else {
                throw new UserException(10000, "unauthorized user");
            }
            chatRepository.save(chatEntity);
            chatId = chatEntity.getChatId();
            subscribeUser(session, fromUserEntity, chatId);
            for (Map.Entry<UserEntity, WebSocketSession> map : onlineDoctors.entrySet()) {
                subscribeUser(map.getValue(), map.getKey(), chatId);
            }
        }

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setChatId(chatId);
        messageEntity.setChatMessageType(chatMessageInputDto.getChatMessageType());
        messageEntity.setMessage(chatMessageInputDto.getMessage());
        messageEntity.setBinary(chatMessageInputDto.getBinary());
        messageEntity.setTimestamp(chatMessageInputDto.getTimestamp());
        messageEntity.setUserId(fromUserEntity.getId());
        messageRepository.save(messageEntity);

        chatMessageInputDto.setChatId(chatId);

        SocketResponseDto responseDto = new SocketResponseDto();
        responseDto.setOk(true);
        responseDto.setShow(true);
        responseDto.setChatMessageDto(chatMessageInputDto);
        if (chatEntityPublisherMap.containsKey(chatId))
            chatEntityPublisherMap.get(chatId).notifySubscribers(fromUserEntity.getId(), responseDto);

        pushNotification(chatMessageInputDto, toUserEntity.orElse(null));

        return chatId;
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
                String data = new ObjectMapper().writeValueAsString(chatMessageInputDto.getMessage() == null ? "یک عکس برای شما ارسال شده است" : chatMessageInputDto.getMessage());
                pushNotificationService.doChat(data, e.getSecond());
            } catch (JsonProcessingException jsonProcessingException) {
                log.info(jsonProcessingException);
            }
        });
    }

    public List<MessageOutputDto> getChatMessagesHistory(int chatId) {
        mustBeActiveIfDoctor();
        List<MessageEntity> messageEntityList = messageRepository.findAllByChatIdOrderByTimestamp(chatId);
        return messageEntityList
                .stream()
                .map(MessageOutputDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ChatOutputDto> getChatHistory() {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        List<ChatEntity> chatEntityList = new ArrayList<>();
        if (userEntity.getUserType() == UserType.Doctor) {
            mustBeActiveIfDoctor(userEntity);
            chatEntityList = chatRepository.findAllByDoctorId(userEntity.getId());
            chatEntityList.addAll(chatRepository
                    .findAllByDoctorIdIsNull());
        } else if (userEntity.getUserType() == UserType.Patient) {
            chatEntityList = chatRepository.findAllByPatientId(userEntity.getId());
        }
        return chatEntityList
                .stream()
                .map(ChatOutputDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<Integer> getUserChatIds() {
        mustBeActiveIfDoctor();
        return getUserChatIds(generalService.getCurrentUserEntity());
    }

    public List<Integer> getUserChatIds(UserEntity userEntity) {
        mustBeActiveIfDoctor(userEntity);
        if (userEntity.getUserType().equals(UserType.Doctor)) {
            List<Integer> doctorChatIds = chatRepository
                    .findAllByDoctorId(userEntity.getId())
                    .stream()
                    .map(ChatEntity::getChatId)
                    .collect(Collectors.toList());
            List<Integer> unAnsweredChatIds = chatRepository
                    .findAllByDoctorIdIsNull()
                    .stream()
                    .map(ChatEntity::getChatId)
                    .collect(Collectors.toList());
            doctorChatIds.addAll(unAnsweredChatIds);
            return doctorChatIds;
        } else if (userEntity.getUserType().equals(UserType.Patient)) {
            return chatRepository
                    .findAllByPatientId(userEntity.getId())
                    .stream()
                    .map(ChatEntity::getChatId)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public void closeChat(int chatId) {
        Optional<ChatEntity> chatEntityOptional = chatRepository.findById(chatId);
        if (chatEntityOptional.isEmpty()) {
            throw new UserException(10000, "chat not found");
        } else if (chatEntityOptional.get().getChatStateType() != ChatStateType.OPEN) {
            throw new UserException(10000, "chat state isn't open");
        } else if (chatEntityOptional.get().getDoctorId() != generalService.getCurrentUserId()) {
            throw new UserException(10000, "you doesn't have access to close this chat");
        }
        chatEntityOptional.get().setChatStateType(ChatStateType.CLOSED);
        chatRepository.save(chatEntityOptional.get());
    }

    private void mustBeActiveIfDoctor() {
        UserEntity userEntity = generalService.getCurrentUserEntity();
        mustBeActiveIfDoctor(userEntity);
    }

    private void mustBeActiveIfDoctor(UserEntity userEntity) {
        if (userEntity.getUserType() == UserType.Doctor) {
            DoctorUserEntity doctorUserEntity = generalService.getCurrentDoctor();
            mustBeActiveIfDoctor(doctorUserEntity);
        }
    }

    private void mustBeActiveIfDoctor(DoctorUserEntity doctorUserEntity) {
        if (doctorUserEntity.getDoctorStateType() != DoctorStateType.ACTIVE) {
            throw new UserException(7013, "your status isn't active.", true);
        }
    }
}
