package ir.beheshti.dandun.base.notification;

import ir.beheshti.dandun.base.firebase.PushNotificationRequest;
import ir.beheshti.dandun.base.firebase.PushNotificationService;
import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.entity.UserNotificationEntity;
import ir.beheshti.dandun.base.user.repository.DoctorRepository;
import ir.beheshti.dandun.base.user.repository.PatientRepository;
import ir.beheshti.dandun.base.user.repository.UserNotificationRepository;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class NotificationScheduledService {

    @Autowired
    private UserNotificationRepository userNotificationRepository;
    @Autowired
    private PushNotificationService pushNotificationService;
    @Autowired
    private NotificationGroupRepository notificationGroupRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    public void pushGreenNotification(int timingId) {
        Optional<NotificationGroupEntity> gr = notificationGroupRepository.findByName(NotificationGroupType.GREEN.getValue());
        if (gr.isEmpty()) {
            log.debug("green notification not found");
            return;
        }

        List<NotificationEntity> notificationEntityList = gr
                .get()
                .getNotificationEntityList()
                .stream()
                .filter(e -> e.getNotificationTimingId() == timingId)
                .collect(Collectors.toList());

        List<PatientUserEntity> greenPatients = patientRepository.findAllByPatientStateType(PatientStateType.GREEN);
        push(notificationEntityList, greenPatients.stream().map(PatientUserEntity::getUserEntity).collect(Collectors.toList()));
    }

    public void pushRedNotification(int timingId) {
        Optional<NotificationGroupEntity> red = notificationGroupRepository.findByName(NotificationGroupType.RED.getValue());
        if (red.isEmpty()) {
            log.debug("red notification not found");
            return;
        }
        List<NotificationEntity> notificationEntityList = red
                .get()
                .getNotificationEntityList()
                .stream()
                .filter(e -> e.getNotificationTimingId() == timingId)
                .collect(Collectors.toList());

        List<PatientUserEntity> patientUserEntities = patientRepository.findAllByPatientStateType(PatientStateType.RED);
        push(notificationEntityList, patientUserEntities.stream().map(PatientUserEntity::getUserEntity).collect(Collectors.toList()));
    }

    public void pushYellowNotification(int timingId) {
        Optional<NotificationGroupEntity> yellow = notificationGroupRepository.findByName(NotificationGroupType.YELLOW.getValue());
        if (yellow.isEmpty()) {
            log.debug("yellow notification not found");
            return;
        }

        List<NotificationEntity> notificationEntityList = yellow
                .get()
                .getNotificationEntityList()
                .stream()
                .filter(e -> e.getNotificationTimingId() == timingId)
                .collect(Collectors.toList());

        List<PatientUserEntity> patientUserEntities = patientRepository.findAllByPatientStateType(PatientStateType.YELLOW);
        push(notificationEntityList, patientUserEntities.stream().map(PatientUserEntity::getUserEntity).collect(Collectors.toList()));
    }

    public void pushDoctorNotification(int timingId) {
        Optional<NotificationGroupEntity> doctor = notificationGroupRepository.findByName(NotificationGroupType.DOCTOR.getValue());
        if (doctor.isEmpty()) {
            log.debug("doctor notification not found");
            return;
        }

        List<NotificationEntity> notificationEntityList = doctor
                .get()
                .getNotificationEntityList()
                .stream()
                .filter(e -> e.getNotificationTimingId() == timingId)
                .collect(Collectors.toList());

        List<DoctorUserEntity> doctorUserEntityList = doctorRepository.findAll();
        push(notificationEntityList, doctorUserEntityList.stream().map(DoctorUserEntity::getUserEntity).collect(Collectors.toList()));
    }

    @Scheduled(cron = "${notification.timing.first}")
    public void pushFirst() {
        pushDoctorNotification(1);
        pushGreenNotification(1);
        pushRedNotification(1);
        pushYellowNotification(1);
    }

    @Scheduled(cron = "${notification.timing.second}")
    public void pushSecond() {
        pushDoctorNotification(2);
        pushGreenNotification(2);
        pushRedNotification(2);
        pushYellowNotification(2);
    }

    @Scheduled(cron = "${notification.timing.third}")
    public void pushThird() {
        pushDoctorNotification(3);
        pushGreenNotification(3);
        pushRedNotification(3);
        pushYellowNotification(3);
    }

    @Scheduled(cron = "${notification.timing.fourth}")
    public void pushFourth() {
        pushDoctorNotification(4);
        pushGreenNotification(4);
        pushRedNotification(4);
        pushYellowNotification(4);
    }

    private void push(List<NotificationEntity> notificationEntities, List<UserEntity> userEntities) {
        for (NotificationEntity notificationEntity : notificationEntities) {
            for (UserEntity userEntity : userEntities) {
                try {
                    PushNotificationRequest request = PushNotificationRequest.sendGeneralToToken(notificationEntity.getTitle(), notificationEntity.getDescription(), userEntity.getNotificationToken());
                    pushNotificationService.sendScheduledNotification(request);

                    UserNotificationEntity userNotificationEntity = new UserNotificationEntity();
                    userNotificationEntity.setUserId(userEntity.getId());
                    userNotificationEntity.setNotificationId(notificationEntity.getNotificationId());
                    userNotificationRepository.save(userNotificationEntity);
                } catch (Exception e) {
                    log.debug("Exception during sending notification", e);
                }
            }
        }
    }
}
