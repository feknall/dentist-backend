package ir.beheshti.dandun.base.user.dto.question;

import ir.beheshti.dandun.base.user.util.QuestionType;
import lombok.Data;

import java.util.List;

@Data
public class QuestionOutputDto {
    private Integer id;
    private String description;
    private QuestionType questionType;
    private List<QuestionChoiceOutputDto> questionChoiceOutputDtoList;
}
