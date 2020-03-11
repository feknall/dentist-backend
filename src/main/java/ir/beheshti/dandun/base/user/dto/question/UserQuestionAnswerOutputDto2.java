package ir.beheshti.dandun.base.user.dto.question;

import ir.beheshti.dandun.base.user.entity.*;
import ir.beheshti.dandun.base.user.util.QuestionType;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class UserQuestionAnswerOutputDto2 {
    private Integer questionId;
    private String questionDescription;
    private QuestionType questionType;
    private String open;
    private Integer value;
    private List<Integer> answerIds;

    public static UserQuestionAnswerOutputDto2 ofOpenAnswer(UserOpenQuestionAnswerEntity entity) {
        UserQuestionAnswerOutputDto2 dto = new UserQuestionAnswerOutputDto2();
        dto.questionId = entity.getEssentialQuestionEntity().getId();
        dto.questionDescription = entity.getEssentialQuestionEntity().getDescription();
        dto.questionType = QuestionType.Open;
        dto.open = entity.getDescription();
        return dto;
    }

    public static UserQuestionAnswerOutputDto2 ofSingle(UserSingleQuestionAnswerEntity entity) {
        UserQuestionAnswerOutputDto2 dto = new UserQuestionAnswerOutputDto2();
        dto.questionId = entity.getEssentialQuestionEntity().getId();
        dto.questionDescription = entity.getEssentialQuestionEntity().getDescription();
        dto.questionType = QuestionType.SingleChoice;
        dto.answerIds = Collections.singletonList(entity.getMultipleChoiceQuestionAnswerId());
        return dto;
    }

    public static UserQuestionAnswerOutputDto2 ofRange(UserRangeQuestionAnswerEntity entity) {
        UserQuestionAnswerOutputDto2 dto = new UserQuestionAnswerOutputDto2();
        dto.questionId = entity.getEssentialQuestionEntity().getId();
        dto.questionDescription = entity.getEssentialQuestionEntity().getDescription();
        dto.questionType = QuestionType.Range;
        dto.value = entity.getValue();
        return dto;
    }

    public static UserQuestionAnswerOutputDto2 ofOpenNumber(UserOpenNumberQuestionAnswerEntity entity) {
        UserQuestionAnswerOutputDto2 dto = new UserQuestionAnswerOutputDto2();
        dto.questionId = entity.getEssentialQuestionEntity().getId();
        dto.questionDescription = entity.getEssentialQuestionEntity().getDescription();
        dto.questionType = QuestionType.OpenNumber;
        dto.value = entity.getValue();
        return dto;
    }

    public static UserQuestionAnswerOutputDto2 ofMultipleChoice(List<UserMultipleChoiceQuestionAnswerEntity> entityList) {
        UserQuestionAnswerOutputDto2 dto = new UserQuestionAnswerOutputDto2();
        dto.questionType = QuestionType.MultipleChoice;
        if (!entityList.isEmpty()) {
            dto.setQuestionId(entityList.get(0).getMultipleChoiceQuestionAnswerEntity().getEssentialQuestionId());
            dto.setQuestionDescription(entityList.get(0).getMultipleChoiceQuestionAnswerEntity().getEssentialQuestionEntity().getDescription());
        }
        dto.answerIds = entityList
                .stream()
                .map(UserMultipleChoiceQuestionAnswerEntity::getId)
                .collect(Collectors.toList());
        return dto;
    }
}
