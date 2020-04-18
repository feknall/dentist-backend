package ir.beheshti.dandun.base.startup.chat;

import ir.beheshti.dandun.base.socket.ChatMessageType;
import ir.beheshti.dandun.base.startup.Insert;
import ir.beheshti.dandun.base.user.entity.*;
import ir.beheshti.dandun.base.user.repository.*;
import ir.beheshti.dandun.base.user.util.DoctorStateType;
import ir.beheshti.dandun.base.user.util.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class ChatStartup implements Insert {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public void insert() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserType(UserType.Patient);
        userRepository.save(userEntity);

        PatientUserEntity patientUserEntity = new PatientUserEntity();
        patientUserEntity.setPatientId(userEntity.getId());
        patientRepository.save(patientUserEntity);

        saveFirst(patientUserEntity);
        saveSecond(patientUserEntity);
    }

    private void saveSecond(PatientUserEntity patientUserEntity) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserType(UserType.Doctor);
        userRepository.save(userEntity);

        DoctorUserEntity secondDoctor = new DoctorUserEntity();
        secondDoctor.setDoctorId(userEntity.getId());
        secondDoctor.setDoctorStateType(DoctorStateType.ACTIVE);
        doctorRepository.save(secondDoctor);

        ChatEntity secondChat = new ChatEntity();
        secondChat.setDoctorId(secondDoctor.getDoctorId());
        secondChat.setPatientId(patientUserEntity.getPatientId());
        secondChat.setActive(true);
        chatRepository.save(secondChat);

        MessageEntity secondMessage = new MessageEntity();
        secondMessage.setMessage("salam! man ye soal daram!");
        secondMessage.setUserId(patientUserEntity.getPatientId());
        secondMessage.setChatId(secondChat.getChatId());
        secondMessage.setTimestamp(10000L);
        secondMessage.setChatMessageType(ChatMessageType.TEXT);
        messageRepository.save(secondMessage);

        secondChat.setMessageId(secondChat.getMessageId());
        chatRepository.save(secondChat);
    }

    private void saveFirst(PatientUserEntity patientUserEntity) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserType(UserType.Patient);
        userRepository.save(userEntity);

        DoctorUserEntity firstDoctor = new DoctorUserEntity();
        firstDoctor.setDoctorId(userEntity.getId());
        firstDoctor.setDoctorStateType(DoctorStateType.ACTIVE);
        doctorRepository.save(firstDoctor);

        ChatEntity firstChat = new ChatEntity();
        firstChat.setDoctorId(firstDoctor.getDoctorId());
        firstChat.setPatientId(patientUserEntity.getPatientId());
        firstChat.setActive(true);
        chatRepository.save(firstChat);

        MessageEntity firstMessage = new MessageEntity();
        firstMessage.setMessage("salam! man ye soal daram!");
        firstMessage.setUserId(patientUserEntity.getPatientId());
        firstMessage.setChatId(firstChat.getChatId());
        firstMessage.setTimestamp(10000L);
        firstMessage.setChatMessageType(ChatMessageType.TEXT);
        messageRepository.save(firstMessage);

        firstChat.setMessageId(firstMessage.getMessageId());
        chatRepository.save(firstChat);
    }
}
