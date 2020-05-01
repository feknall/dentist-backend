package ir.beheshti.dandun.base.notification;

import lombok.Data;

@Data
public class NotificationInputDto {
    private String title;
    private String description;
    private String image;

    public FaqEntity toEntity() {
        FaqEntity entity = new FaqEntity();
        entity.setTitle(this.getTitle());
        entity.setDescription(this.getDescription());
        entity.setImage(this.getImage());
        return entity;
    }
}
