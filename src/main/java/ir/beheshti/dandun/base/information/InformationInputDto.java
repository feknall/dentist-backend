package ir.beheshti.dandun.base.information;

import lombok.Data;

@Data
public class InformationInputDto {
    private String title;
    private String description;
    private String image;

    public InformationEntity toEntity() {
        InformationEntity entity = new InformationEntity();
        entity.setTitle(this.getTitle());
        entity.setDescription(this.getDescription());
        entity.setImage(this.getImage());
        return entity;
    }
}
