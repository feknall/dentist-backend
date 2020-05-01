package ir.beheshti.dandun.base.notification;

import ir.beheshti.dandun.base.faq.FaqEntity;
import ir.beheshti.dandun.base.information.InformationEntity;
import ir.beheshti.dandun.base.user.common.ErrorCodeAndMessage;
import ir.beheshti.dandun.base.user.common.UserException;
import ir.beheshti.dandun.base.user.repository.UserNotificationRepository;
import ir.beheshti.dandun.base.user.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserNotificationRepository userNotificationRepository;
    @Autowired
    private GeneralService generalService;

    public List<NotificationOutputDto> getAllNotifications() {
        return notificationRepository
                .findAll()
                .stream()
                .map(NotificationOutputDto::fromEntity)
                .collect(Collectors.toList());
    }

    public NotificationOutputDto getNotificationById(int id) {
        Optional<NotificationEntity> entity = notificationRepository.findById(id);
        if (entity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.FAQ_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.FAQ_NOT_FOUND_MESSAGE);
        }
        return NotificationOutputDto.fromEntity(entity.get());
    }

    public void addNotification(NotificationInputDto notificationInputDto) {
        notificationRepository.save(notificationInputDto.toEntity());
    }

    public List<NotificationOutputDto> getUserNotificationList() {
       return userNotificationRepository
               .findAllByUserId(generalService.getCurrentUserId())
               .stream()
                .map(e -> NotificationOutputDto.fromEntity(e.getNotificationEntity()))
                .collect(Collectors.toList());
    }

    public void updateNotification(NotificationInputDto notificationInputDto) {
        Optional<NotificationEntity> entity = notificationRepository.findById(notificationInputDto.getNotificationId());
        if (entity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.NOTIFICATION_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.NOTIFICATION_NOT_FOUND_MESSAGE);
        }
        NotificationEntity notificationEntity = notificationInputDto.toEntity();
        notificationEntity.setNotificationId(notificationInputDto.getNotificationId());
        notificationRepository.save(notificationEntity);
    }

    public void deleteNotification(int notificationId) {
        Optional<NotificationEntity> entity = notificationRepository.findById(notificationId);
        if (entity.isEmpty()) {
            throw new UserException(ErrorCodeAndMessage.NOTIFICATION_NOT_FOUND_CODE,
                    ErrorCodeAndMessage.NOTIFICATION_NOT_FOUND_MESSAGE);
        }
        notificationRepository.deleteById(notificationId);
    }
}
