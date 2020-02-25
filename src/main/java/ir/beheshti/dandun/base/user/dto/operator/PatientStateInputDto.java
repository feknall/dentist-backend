package ir.beheshti.dandun.base.user.dto.operator;

import ir.beheshti.dandun.base.user.util.PatientStateType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PatientStateInputDto {
    private int patientId;

    @NotNull
    private PatientStateType patientStateType;
}
