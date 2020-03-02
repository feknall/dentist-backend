package ir.beheshti.dandun.base.user.dto.operator;

import ir.beheshti.dandun.base.user.util.DoctorStateType;
import ir.beheshti.dandun.base.user.util.PatientStateType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DoctorStateInputDto {
    private int doctorId;
    @NotNull
    private DoctorStateType doctorStateType;
}
