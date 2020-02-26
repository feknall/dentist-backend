package ir.beheshti.dandun.base.user.dto.question;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AllAnswerOpenDto {
    @NotNull
    private List<OpenAnswerInputDto> openAnswerInputDtoList;
    @NotNull
    private List<TrueFalseAnswerInputDto> trueFalseAnswerInputDtoList;
    @NotNull
    private List<MultipleChoiceAnswerInputDto> multipleChoiceAnswerInputDtoList;
}
