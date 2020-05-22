package ir.beheshti.dandun.base.notification;

import lombok.Data;

@Data
public class NotificationTimingOutputDto {
    private Integer id;
    private String name;

    public static NotificationTimingOutputDto from(NotificationTimingEntity entity) {
        NotificationTimingOutputDto dto = new NotificationTimingOutputDto();
        dto.id = entity.getNotificationTimingId();
        dto.name = entity.getName();
        return dto;
    }
}
