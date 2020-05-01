package ir.beheshti.dandun.base.notification;

import lombok.Data;

@Data
public class NotificationOutputDto {
    private int notificationId;
    private String title;
    private String description;
    private String image;

    public static NotificationOutputDto fromEntity(NotificationEntity entity) {
        NotificationOutputDto dto = new NotificationOutputDto();
        dto.setNotificationId(entity.getNotificationId());
        dto.setTitle(entity.getTitle());
        dto.setImage(entity.getImage());
        return dto;
    }

}
