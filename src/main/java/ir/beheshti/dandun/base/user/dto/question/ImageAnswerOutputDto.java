package ir.beheshti.dandun.base.user.dto.question;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ImageAnswerOutputDto {
    private Integer questionId;
    private Integer imageId;
    private String image;
}
