package ir.beheshti.dandun.base.notification;

import ir.beheshti.dandun.base.firebase.PushNotificationRequest;
import ir.beheshti.dandun.base.firebase.PushNotificationService;
import ir.beheshti.dandun.base.user.entity.DoctorUserEntity;
import ir.beheshti.dandun.base.user.entity.PatientUserEntity;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.DoctorRepository;
import ir.beheshti.dandun.base.user.repository.PatientRepository;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Configuration
@EnableScheduling
public class NotificationScheduledService {

    @Autowired
    private PushNotificationService pushNotificationService;
    @Autowired
    private NotificationGroupRepository notificationGroupRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Scheduled(cron = "${notification.green.cron}")
    public void pushGreenNotification() {
        Optional<NotificationGroupEntity> gr = notificationGroupRepository.findByName(NotificationGroupType.GREEN.getValue());
        if (gr.isEmpty()) {
            log.debug("green notification not found");
            return;
        }
        List<NotificationEntity> notificationEntityList = gr.get().getNotificationEntityList();
        List<PatientUserEntity> greenPatients = patientRepository.findAllByPatientStateType(PatientStateType.GREEN);
        push(notificationEntityList, greenPatients.stream().map(PatientUserEntity::getUserEntity).collect(Collectors.toList()));
    }

    @Scheduled(cron = "${notification.red.cron}")
    public void pushRedNotification() {
        Optional<NotificationGroupEntity> red = notificationGroupRepository.findByName(NotificationGroupType.RED.getValue());
        if (red.isEmpty()) {
            log.debug("red notification not found");
            return;
        }
        List<NotificationEntity> notificationEntityList = red.get().getNotificationEntityList();
        List<PatientUserEntity> patientUserEntities = patientRepository.findAllByPatientStateType(PatientStateType.RED);
        push(notificationEntityList, patientUserEntities.stream().map(PatientUserEntity::getUserEntity).collect(Collectors.toList()));
    }

    @Scheduled(cron = "${notification.yellow.cron}")
    public void pushYellowNotification() {
        Optional<NotificationGroupEntity> yellow = notificationGroupRepository.findByName(NotificationGroupType.YELLOW.getValue());
        if (yellow.isEmpty()) {
            log.debug("yellow notification not found");
            return;
        }
        List<NotificationEntity> notificationEntityList = yellow.get().getNotificationEntityList();
        List<PatientUserEntity> patientUserEntities = patientRepository.findAllByPatientStateType(PatientStateType.YELLOW);
        push(notificationEntityList, patientUserEntities.stream().map(PatientUserEntity::getUserEntity).collect(Collectors.toList()));
    }

    @Scheduled(cron = "${notification.doctor.cron}")
    public void pushDoctorNotification() {
        Optional<NotificationGroupEntity> doctor = notificationGroupRepository.findByName(NotificationGroupType.DOCTOR.getValue());
        if (doctor.isEmpty()) {
            log.debug("doctor notification not found");
            return;
        }
        List<NotificationEntity> notificationEntityList = doctor.get().getNotificationEntityList();
        List<DoctorUserEntity> doctorUserEntityList = doctorRepository.findAll();
        push(notificationEntityList, doctorUserEntityList.stream().map(DoctorUserEntity::getUserEntity).collect(Collectors.toList()));
    }

    private void push(List<NotificationEntity> notificationEntities, List<UserEntity> userEntities) {
        for (NotificationEntity notificationEntity: notificationEntities) {
            for (UserEntity userEntity: userEntities) {
                try {
                    PushNotificationRequest request = PushNotificationRequest.sendGeneralToToken(notificationEntity.getTitle(), notificationEntity.getDescription(), userEntity.getNotificationToken());
                    pushNotificationService.sendScheduledNotification(request);
                } catch (Exception e) {
                    log.debug("Exception during sending notification", e);
                }
            }
        }
    }
}
