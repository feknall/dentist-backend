package ir.beheshti.dandun.base.notification;

import lombok.Data;

@Data
public class NotificationGroupOutputDto {
    private Integer id;
    private String name;

    public static NotificationGroupOutputDto from(NotificationGroupEntity entity) {
        NotificationGroupOutputDto dto = new NotificationGroupOutputDto();
        dto.setId(entity.getNotificationGroupId());
        dto.setName(entity.getName());
        return dto;
    }
}
