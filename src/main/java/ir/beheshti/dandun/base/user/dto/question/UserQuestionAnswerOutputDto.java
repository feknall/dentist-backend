package ir.beheshti.dandun.base.user.dto.question;

import ir.beheshti.dandun.base.user.entity.*;
import ir.beheshti.dandun.base.user.util.QuestionType;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserQuestionAnswerOutputDto {
    private Integer questionId;
    private String questionDescription;
    private QuestionType questionType;
    private String open;
    private Integer value;
    private List<MultipleChoiceAnswerOutputDto2> answers;

    public static UserQuestionAnswerOutputDto ofOpenAnswer(UserOpenQuestionAnswerEntity entity) {
        UserQuestionAnswerOutputDto dto = new UserQuestionAnswerOutputDto();
        dto.questionId = entity.getEssentialQuestionEntity().getId();
        dto.questionDescription = entity.getEssentialQuestionEntity().getDescription();
        dto.questionType = QuestionType.Open;
        dto.open = entity.getDescription();
        return dto;
    }

    public static UserQuestionAnswerOutputDto ofSingle(UserSingleQuestionAnswerEntity entity) {
        UserQuestionAnswerOutputDto dto = new UserQuestionAnswerOutputDto();
        dto.questionId = entity.getMultipleChoiceQuestionAnswerEntity() != null ? entity.getMultipleChoiceQuestionAnswerEntity().getEssentialQuestionId() : null;
        dto.questionDescription = entity.getMultipleChoiceQuestionAnswerEntity() != null ? entity.getMultipleChoiceQuestionAnswerEntity().getEssentialQuestionEntity().getDescription() : null;
        dto.questionType = QuestionType.SingleChoice;

        MultipleChoiceAnswerOutputDto2 answer = new MultipleChoiceAnswerOutputDto2();
        answer.setAnswerId(entity.getMultipleChoiceQuestionAnswerId());
        answer.setDescription(entity.getMultipleChoiceQuestionAnswerEntity().getDescription());
        dto.answers = Collections.singletonList(answer);

        return dto;
    }

    public static UserQuestionAnswerOutputDto ofRange(UserRangeQuestionAnswerEntity entity) {
        UserQuestionAnswerOutputDto dto = new UserQuestionAnswerOutputDto();
        dto.questionId = entity.getEssentialQuestionEntity().getId();
        dto.questionDescription = entity.getEssentialQuestionEntity().getDescription();
        dto.questionType = QuestionType.Range;
        dto.value = entity.getValue();
        return dto;
    }

    public static UserQuestionAnswerOutputDto ofOpenNumber(UserOpenNumberQuestionAnswerEntity entity) {
        UserQuestionAnswerOutputDto dto = new UserQuestionAnswerOutputDto();
        dto.questionId = entity.getEssentialQuestionEntity().getId();
        dto.questionDescription = entity.getEssentialQuestionEntity().getDescription();
        dto.questionType = QuestionType.OpenNumber;
        dto.value = entity.getValue();
        return dto;
    }

    public static UserQuestionAnswerOutputDto ofMultipleChoice(List<UserMultipleChoiceQuestionAnswerEntity> entityList) {
        UserQuestionAnswerOutputDto dto = new UserQuestionAnswerOutputDto();
        dto.questionType = QuestionType.MultipleChoice;
        if (!entityList.isEmpty()) {
            dto.setQuestionId(entityList.get(0).getMultipleChoiceQuestionAnswerEntity().getEssentialQuestionId());
            dto.setQuestionDescription(entityList.get(0).getMultipleChoiceQuestionAnswerEntity().getEssentialQuestionEntity().getDescription());
        }
        dto.answers = entityList
                .stream()
                .map(e -> {
                    MultipleChoiceAnswerOutputDto2 dto2 = new MultipleChoiceAnswerOutputDto2();
                    dto2.setAnswerId(e.getMultipleChoiceQuestionAnswerId());
                    dto2.setDescription(e.getMultipleChoiceQuestionAnswerEntity().getDescription());
                    return dto2;
                })
                .collect(Collectors.toList());
        return dto;
    }
}
