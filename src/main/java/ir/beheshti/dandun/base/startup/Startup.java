package ir.beheshti.dandun.base.startup;

import ir.beheshti.dandun.base.startup.question.DefaultStartup;
import ir.beheshti.dandun.base.startup.question.DoctorStartup;
import ir.beheshti.dandun.base.startup.question.PatientStartup;
import ir.beheshti.dandun.base.startup.user.DoctorUser;
import ir.beheshti.dandun.base.startup.user.PatientUser;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final InformationStartup informationStartup;

    @Autowired
    private DoctorUser doctorUser;
    @Autowired
    private PatientUser patientUser;

    @Value("${dandun.questions.insert}")
    private Boolean insertQuestions;

    @Value("${dandun.information.insert}")
    private Boolean insertInformation;

    @Value("${dandun.user.insert}")
    private Boolean insertUser;

    public Startup(DefaultStartup defaultStartup, DoctorStartup doctorStartup,
                   OperatorStartup operatorStartup, PatientStartup patientStartup, InformationStartup informationStartup) {
        this.defaultStartup = defaultStartup;
        this.doctorStartup = doctorStartup;
        this.operatorStartup = operatorStartup;
        this.patientStartup = patientStartup;
        this.informationStartup = informationStartup;
    }

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        if (insertInformation) {
            informationStartup.insert();
        }
        if (insertUser) {
            doctorUser.insert();
            patientUser.insert();
        }
        if (!insertQuestions)
            return;
        defaultStartup.insert();
        doctorStartup.insert();
        patientStartup.insert();
        operatorStartup.insert();
    }
}
