package ir.beheshti.dandun.base.user.dto.question;

import ir.beheshti.dandun.base.user.entity.UserMultipleChoiceQuestionAnswerEntity;
import ir.beheshti.dandun.base.user.entity.UserOpenQuestionAnswerEntity;
import ir.beheshti.dandun.base.user.entity.UserTrueFalseQuestionAnswerEntity;
import ir.beheshti.dandun.base.user.util.QuestionType;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserQuestionAnswerOutputDto {
    private Integer questionId;
    private QuestionType questionType;
    private Boolean trueFalseAnswer;
    private String openAnswer;
    private List<MultipleChoiceAnswerOutputDto> multipleChoiceAnswer;

    public static UserQuestionAnswerOutputDto ofOpenAnswer(UserOpenQuestionAnswerEntity entity) {
        UserQuestionAnswerOutputDto dto = new UserQuestionAnswerOutputDto();
        dto.questionId = entity.getEssentialQuestionEntity().getId();
        dto.questionType = QuestionType.Open;
        dto.openAnswer = entity.getDescription();
        return dto;
    }

    public static UserQuestionAnswerOutputDto ofTrueFalse(UserTrueFalseQuestionAnswerEntity entity) {
        UserQuestionAnswerOutputDto dto = new UserQuestionAnswerOutputDto();
        dto.questionId = entity.getEssentialQuestionEntity().getId();
        dto.questionType = QuestionType.TrueFalse;
        dto.trueFalseAnswer = entity.isAnswer();
        return dto;
    }

    public static UserQuestionAnswerOutputDto ofMultipleChoice(List<UserMultipleChoiceQuestionAnswerEntity> entityList) {
        UserQuestionAnswerOutputDto dto = new UserQuestionAnswerOutputDto();
        dto.questionType = QuestionType.MultipleChoice;
        Set<Integer> questionIds = new HashSet<>();
        for (UserMultipleChoiceQuestionAnswerEntity entity : entityList) {
            questionIds.add(entity.getMultipleChoiceQuestionAnswerEntity().getEssentialQuestionId());
        }

        List<MultipleChoiceAnswerOutputDto> multipleChoiceAnswer = new ArrayList<>();
        for (int questionId : questionIds) {
            MultipleChoiceAnswerOutputDto multipleChoiceAnswerOutputDto = new MultipleChoiceAnswerOutputDto();
            multipleChoiceAnswerOutputDto.setQuestionId(questionId);
            List<Integer> answerIdList = new ArrayList<>();
            entityList.forEach(e -> {
                if (questionId == e.getMultipleChoiceQuestionAnswerEntity().getEssentialQuestionId()) {
                    answerIdList.add(e.getMultipleChoiceQuestionAnswerId());
                }
            });
            multipleChoiceAnswerOutputDto.setAnswerIdList(answerIdList);
            multipleChoiceAnswer.add(multipleChoiceAnswerOutputDto);
        }
        dto.multipleChoiceAnswer = multipleChoiceAnswer;

        return dto;
    }
}
