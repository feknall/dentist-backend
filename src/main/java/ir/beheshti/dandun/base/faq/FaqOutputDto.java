package ir.beheshti.dandun.base.faq;

import lombok.Data;

@Data
public class FaqOutputDto {
    private int faqId;
    private String title;
    private String description;
    private String image;

    public static FaqOutputDto fromEntityMinimum(FaqEntity entity) {
        FaqOutputDto dto = new FaqOutputDto();
        dto.setFaqId(entity.getFaqId());
        dto.setTitle(entity.getTitle());
        dto.setImage(entity.getImage());
        return dto;
    }

    public static FaqOutputDto fromEntityFull(FaqEntity entity) {
        FaqOutputDto dto = new FaqOutputDto();
        dto.setFaqId(entity.getFaqId());
        dto.setTitle(entity.getTitle());
        dto.setImage(entity.getImage());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
