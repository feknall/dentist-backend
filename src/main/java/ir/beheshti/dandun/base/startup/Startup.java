package ir.beheshti.dandun.base.startup;

import ir.beheshti.dandun.base.startup.notification.NotificationStartup;
import ir.beheshti.dandun.base.startup.question.CommonStartup;
import ir.beheshti.dandun.base.startup.question.DoctorStartup;
import ir.beheshti.dandun.base.startup.question.PatientStartup;
import ir.beheshti.dandun.base.startup.user.DoctorUser;
import ir.beheshti.dandun.base.startup.user.PatientUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Startup {

    @Autowired
    private CommonStartup commonStartup;
    @Autowired
    private DoctorStartup doctorStartup;
    @Autowired
    private OperatorStartup operatorStartup;
    @Autowired
    private PatientStartup patientStartup;
    @Autowired
    private InformationStartup informationStartup;
    @Autowired
    private DoctorUser doctorUser;
    @Autowired
    private PatientUser patientUser;
    @Autowired
    private NotificationStartup notificationStartup;

    @Value("${dandun.questions.insert}")
    private Boolean insertQuestions;

    @Value("${dandun.information.insert}")
    private Boolean insertInformation;

    @Value("${dandun.user.insert}")
    private Boolean insertUser;

    @Value("${dandun.notification.insert}")
    private Boolean insertNotification;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        if (insertNotification) {
            notificationStartup.insert();
        }
        if (insertInformation) {
            informationStartup.insert();
        }
        if (insertUser) {
            doctorUser.insert();
            patientUser.insert();
        }

        if (insertQuestions) {
            commonStartup.insert();
            doctorStartup.insert();
            patientStartup.insert();
            operatorStartup.insert();
        }
    }
}
