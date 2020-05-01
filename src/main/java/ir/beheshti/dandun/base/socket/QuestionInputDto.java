package ir.beheshti.dandun.base.socket;

import lombok.Data;

@Data
public class QuestionInputDto {
    private String title;
    private String description;
    private String[] images;
}
