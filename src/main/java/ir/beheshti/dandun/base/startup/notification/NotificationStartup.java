package ir.beheshti.dandun.base.startup.notification;

import ir.beheshti.dandun.base.startup.Insert;
import ir.beheshti.dandun.base.user.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationStartup implements Insert {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void insert() {
        NotificationEntity ent1 = new NotificationEntity();
        ent1.setTitle("title1");
        ent1.setDescription("desc1");
        notificationRepository.save(ent1);

        NotificationEntity ent2 = new NotificationEntity();
        ent2.setTitle("title2");
        ent2.setDescription("desc2");
        notificationRepository.save(ent2);
    }
}
