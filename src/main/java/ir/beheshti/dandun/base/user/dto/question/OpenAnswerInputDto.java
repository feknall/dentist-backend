package ir.beheshti.dandun.base.user.dto.question;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class OpenAnswerInputDto {
    @NotNull
    private Integer questionId;
    @NotEmpty
    private String description;
}
