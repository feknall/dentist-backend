package ir.beheshti.dandun.base.user.dto.operator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class OperatorLoginInputDto {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
