package ir.beheshti.dandun.base.notification;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class NotificationInputDto {
    private int notificationId;
    private String title;
    private String description;
    private String image;
    private int timingId;
    private List<Integer> groupIdList;

    public NotificationEntity toEntity() {
        NotificationEntity entity = new NotificationEntity();
        entity.setTitle(this.getTitle());
        entity.setDescription(this.getDescription());
        entity.setImage(this.getImage());
        entity.setNotificationTimingId(this.getTimingId());
        entity.setNotificationGroupEntityList(groupIdList
                .stream()
                .map(e -> {
                    NotificationGroupEntity groupEntity = new NotificationGroupEntity();
                    groupEntity.setNotificationGroupId(e);
                    return groupEntity;
                })
                .collect(Collectors.toList()));
        return entity;
    }
}
