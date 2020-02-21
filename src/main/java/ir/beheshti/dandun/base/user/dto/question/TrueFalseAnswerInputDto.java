package ir.beheshti.dandun.base.user.dto.question;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TrueFalseAnswerInputDto {
    @NotNull
    private Integer questionId;
    @NotNull
    private Boolean answer;
}
