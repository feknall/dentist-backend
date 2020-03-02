package ir.beheshti.dandun.base.startup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Startup {

    private final DefaultStartup defaultStartup;
    private final DoctorStartup doctorStartup;
    private final OperatorStartup operatorStartup;
    private final PatientStartup patientStartup;

    @Value("${dandun.questions.insert}")
    private Boolean insertQuestions;

    public Startup(DefaultStartup defaultStartup, DoctorStartup doctorStartup,
                   OperatorStartup operatorStartup, PatientStartup patientStartup) {
        this.defaultStartup = defaultStartup;
        this.doctorStartup = doctorStartup;
        this.operatorStartup = operatorStartup;
        this.patientStartup = patientStartup;
    }

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        if (!insertQuestions)
            return;
        defaultStartup.insert();
        doctorStartup.insert();
        patientStartup.insert();
        operatorStartup.insert();
    }
}
