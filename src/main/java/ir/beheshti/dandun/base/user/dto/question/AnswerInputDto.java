package ir.beheshti.dandun.base.user.dto.question;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnswerInputDto {
    private Integer questionId;
    private Integer zeroToFile;
    private Boolean trueFalse;
    private String description;
    private Integer questionChoiceId;
}
