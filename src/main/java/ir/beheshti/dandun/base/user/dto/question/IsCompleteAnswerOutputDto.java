package ir.beheshti.dandun.base.user.dto.question;

import ir.beheshti.dandun.base.user.util.PatientStateType;
import lombok.Data;

@Data
public class IsCompleteAnswerOutputDto {
    private boolean isComplete;
    private PatientStateType patientStateType;
}
