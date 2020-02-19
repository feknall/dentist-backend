package ir.beheshti.dandun.base.user.dto.question;

import ir.beheshti.dandun.base.user.entity.MultipleChoiceQuestionAnswerEntity;
import lombok.Data;

@Data
public class QuestionChoiceAnswerOutputDto {
    private Integer id;
    private String description;

    public static QuestionChoiceAnswerOutputDto fromEntity(MultipleChoiceQuestionAnswerEntity entity) {
        QuestionChoiceAnswerOutputDto outputDto = new QuestionChoiceAnswerOutputDto();
        outputDto.setId(entity.getId());
        outputDto.setDescription(entity.getDescription());
        return outputDto;
    }
}
