package ir.beheshti.dandun.base.socket;

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

    private Map<Integer, UserEntity> onlinePatients = new HashMap<>();
    private Map<Integer, UserEntity> onlineDoctors = new HashMap<>();

    // chatId - chatEntity
    private Map<Integer, ChatEntityPublisher> chatEntityPublisherMap = new HashMap<>();

    public void addOnlineUser(UserEntity userEntity, WebSocketSession session) {
        if (userEntity.getUserType() == UserType.Doctor) {
            onlineDoctors.put(userEntity.getId(), userEntity);
        } else if (userEntity.getUserType() == UserType.Patient) {
            onlinePatients.put(userEntity.getId(), userEntity);
        }
    }

    public void removeOnlineUser(UserEntity userEntity) {
        if (userEntity.getUserType() == UserType.Doctor && onlineDoctors.containsKey(userEntity.getId())) {
            onlineDoctors.remove(userEntity.getId());
        } else if (userEntity.getUserType() == UserType.Patient && onlinePatients.containsKey(userEntity.getId())) {
            onlinePatients.remove(userEntity.getId());
        }
    }

    public void unsubscribeUser(UserEntity userEntity, Integer chatId) {
        if (chatEntityPublisherMap.containsKey(chatId)) {
            chatEntityPublisherMap.get(chatId).removeSubscriber(userEntity.getId());
        }
    }

    public void subscribeUser(WebSocketSession session, UserEntity userEntity, Integer chatId) {
        userEntity.setSession(session);
        if (userEntity.getUserType() != UserType.Doctor && userEntity.getUserType() != UserType.Patient) {
            throw new UserException(1000, "Unknown user trying to connect");
        }
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
        ChatEntity chatEntity;
        if (chatMessageInputDto.getChatId() != null) {
            Optional<ChatEntity> oldChatEntity = chatRepository.findById(chatMessageInputDto.getChatId());
            if (oldChatEntity.isEmpty()) {
                throw new UserException(10000, "chat entity not found.");
            }
            chatEntity = oldChatEntity.get();
            if (chatEntity.getChatStateType() == ChatStateType.CLOSED) {
                throw new UserException(CHAT_IS_CLOSED_CODE, "chat is closed");
            } else if (fromUserEntity.getUserType() == UserType.Patient) {
                if (chatEntity.getPatientId() != fromUserEntity.getId()) {
                    throw new UserException(10000, "this patient is not allowed to send message in this chat.");
                }
            } else if (fromUserEntity.getUserType() == UserType.Doctor) {
                if (chatEntity.getDoctorId() != null && chatEntity.getDoctorId() != fromUserEntity.getId()) {
                    throw new UserException(10000, "this doctor is not allowed to send message in this chat.");
                }
                toUserEntity = Optional.of(chatEntity.getPatientEntity());
                chatEntity.setDoctorId(fromUserEntity.getId());
                chatEntity.setChatStateType(ChatStateType.OPEN);
                chatRepository.save(chatEntity);
                subscribeUser(session, fromUserEntity, chatEntity.getChatId());
            }
            chatId = chatEntity.getChatId();
        } else {
            chatEntity = new ChatEntity();
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
            for (Map.Entry<Integer, UserEntity> map : onlineDoctors.entrySet()) {
                subscribeUser(map.getValue().getSession(), map.getValue(), chatId);
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
        responseDto.setChatOutputDto(ChatOutputDto.fromEntity(chatEntity, messageEntity));
        if (chatEntityPublisherMap.containsKey(chatId))
            chatEntityPublisherMap.get(chatId).notifySubscribers(fromUserEntity.getId(), responseDto);

        try {
            pushNotification(ChatOutputDto.fromEntity(chatEntity, messageEntity), toUserEntity.orElse(null));
        } catch (Exception e) {
            log.debug("Error during pushing notification", e);
        }
        return chatId;
    }

    public void pushNotification(ChatOutputDto chatOutputDto, UserEntity toUserEntity) {
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
            pushNotificationService.doChat(e.getSecond(), chatOutputDto);
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
            Optional<DoctorUserEntity> doctorUserEntity = doctorRepository.findById(userEntity.getId());
            mustBeActiveIfDoctor(doctorUserEntity.get());
        }
    }

    private void mustBeActiveIfDoctor(DoctorUserEntity doctorUserEntity) {
        if (doctorUserEntity.getDoctorStateType() != DoctorStateType.ACTIVE) {
            throw new UserException(7013, "your status isn't active.", true);
        }
    }
}
