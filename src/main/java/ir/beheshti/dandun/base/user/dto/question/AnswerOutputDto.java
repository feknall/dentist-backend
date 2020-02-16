package ir.beheshti.dandun.base.user.dto.question;

import lombok.Data;

@Data
public class AnswerOutputDto {
    private Integer questionId;
    private Integer zeroToFile;
    private Boolean trueFalse;
    private String description;
    private Integer questionChoiceId;
}
