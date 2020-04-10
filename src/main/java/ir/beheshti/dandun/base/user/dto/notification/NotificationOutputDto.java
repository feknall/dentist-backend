package ir.beheshti.dandun.base.user.dto.notification;

import ir.beheshti.dandun.base.user.entity.NotificationEntity;
import lombok.Data;


@Data
public class NotificationOutputDto {
    private String title;
    private String description;
    private String image;

    public static NotificationOutputDto fromEntity(NotificationEntity notificationEntity) {
        NotificationOutputDto outputDto = new NotificationOutputDto();
        outputDto.setTitle(notificationEntity.getTitle());
        outputDto.setDescription(notificationEntity.getDescription());
        outputDto.setImage(notificationEntity.getImage());
        return outputDto;
    }
}
