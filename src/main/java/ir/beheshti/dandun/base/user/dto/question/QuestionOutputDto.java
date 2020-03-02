package ir.beheshti.dandun.base.user.dto.question;

import ir.beheshti.dandun.base.user.entity.EssentialQuestionEntity;
import ir.beheshti.dandun.base.user.util.QuestionType;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuestionOutputDto {
    private Integer id;
    private String description;
    private QuestionType questionType;
    private Integer dependAnswerOnId;
    private List<QuestionChoiceAnswerOutputDto> questionChoiceAnswerOutputDtoList;

    public static QuestionOutputDto fromEntity(EssentialQuestionEntity entity) {
        QuestionOutputDto outputDto = new QuestionOutputDto();
        outputDto.setId(entity.getId());
        outputDto.setDescription(entity.getDescription());
        outputDto.setQuestionType(entity.getQuestionType());
        outputDto.setDependAnswerOnId(entity.getDependOnAnswerId());
        outputDto.setQuestionChoiceAnswerOutputDtoList(entity
                .getMultipleChoiceQuestionAnswerEntityList()
                .stream()
                .map(QuestionChoiceAnswerOutputDto::fromEntity)
                .collect(Collectors.toList()));
        return outputDto;
    }
}
