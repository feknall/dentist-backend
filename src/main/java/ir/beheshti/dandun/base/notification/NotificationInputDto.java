package ir.beheshti.dandun.base.notification;

import lombok.Data;

@Data
public class NotificationInputDto {
    private int notificationId;
    private String title;
    private String description;
    private String image;

    public NotificationEntity toEntity() {
        NotificationEntity entity = new NotificationEntity();
        entity.setTitle(this.getTitle());
        entity.setDescription(this.getDescription());
        entity.setImage(this.getImage());
        return entity;
    }
}
