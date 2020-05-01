package ir.beheshti.dandun.base.information;

import lombok.Data;

@Data
public class InformationOutputDto {
    private int informationId;
    private String title;
    private String description;
    private String image;
    public static InformationOutputDto fromEntityMinimum(InformationEntity entity) {
        InformationOutputDto dto = new InformationOutputDto();
        dto.setInformationId(entity.getInformationId());
        dto.setTitle(entity.getTitle());
        dto.setImage(entity.getImage());
        return dto;
    }

    public static InformationOutputDto fromEntity(InformationEntity entity) {
        InformationOutputDto dto = new InformationOutputDto();
        dto.setInformationId(entity.getInformationId());
        dto.setTitle(entity.getTitle());
        dto.setImage(entity.getImage());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
