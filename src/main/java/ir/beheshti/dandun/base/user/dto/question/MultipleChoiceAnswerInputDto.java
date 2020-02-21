package ir.beheshti.dandun.base.user.dto.question;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MultipleChoiceAnswerInputDto {
    @NotNull
    private Integer questionId;
    @Valid
    private List<Integer> answerIdList;
}
