package ir.beheshti.dandun.base.notification;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class NotificationOutputDto {
    private int notificationId;
    private String title;
    private String description;
    private String image;
    private String timing;
    private List<NotificationGroupOutputDto> notificationOutputDtoList;

    public static NotificationOutputDto fromEntity(NotificationEntity entity) {
        NotificationOutputDto dto = new NotificationOutputDto();
        dto.setNotificationId(entity.getNotificationId());
        dto.setTitle(entity.getTitle());
        dto.setImage(entity.getImage());
        dto.setTiming(entity.getNotificationTimingEntity().getName());
        dto.setNotificationOutputDtoList(entity
                .getNotificationGroupEntityList()
                .stream()
                .map(NotificationGroupOutputDto::from)
                .collect(Collectors.toList()));
        return dto;
    }

}
