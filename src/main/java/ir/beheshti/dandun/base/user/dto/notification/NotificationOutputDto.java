package ir.beheshti.dandun.base.user.dto.notification;

import ir.beheshti.dandun.base.user.entity.NotificationEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class NotificationOutputDto {
    private String title;
    private String description;
    private String image;

    public static NotificationOutputDto fromEntity(NotificationEntity notificationEntity) {
        NotificationOutputDto outputDto = new NotificationOutputDto();
        BeanUtils.copyProperties(notificationEntity, outputDto);
        return outputDto;
    }
}
