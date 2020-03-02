package ir.beheshti.dandun.base.user.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserImageInputDto {
    @NotNull
    private String image;
}
