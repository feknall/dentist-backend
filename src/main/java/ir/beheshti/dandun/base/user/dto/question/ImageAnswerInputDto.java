package ir.beheshti.dandun.base.user.dto.question;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ImageAnswerInputDto {
    @NotNull
    private Integer questionId;
    @NotNull
    private List<String> imageList;
}
