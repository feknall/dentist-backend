package ir.beheshti.dandun.base.startup.notification;

import ir.beheshti.dandun.base.notification.*;
import ir.beheshti.dandun.base.startup.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class NotificationStartup implements Insert {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationTimingRepository notificationTimingRepository;
    @Autowired
    private NotificationGroupRepository notificationGroupRepository;
    @Override
    public void insert() {
        NotificationTimingEntity charshanbeHa = new NotificationTimingEntity();
        charshanbeHa.setName("چهارشنبه‌ شب‌ها");
        charshanbeHa.setCron("*");
        notificationTimingRepository.save(charshanbeHa);

        NotificationTimingEntity panjshanbeHa = new NotificationTimingEntity();
        panjshanbeHa.setName("پنجشنبه‌ شب‌ها");
        panjshanbeHa.setCron("*");
        notificationTimingRepository.save(panjshanbeHa);

        NotificationGroupEntity group1 = new NotificationGroupEntity();
        group1.setName("زردها");

        NotificationGroupEntity group2 = new NotificationGroupEntity();
        group2.setName("دکترها");

        List<NotificationGroupEntity> groupEntityList = Arrays.asList(group1, group2);
        notificationGroupRepository.saveAll(groupEntityList);

        NotificationEntity ent1 = new NotificationEntity();
        ent1.setTitle("title1");
        ent1.setDescription("desc1");
        ent1.setNotificationTimingId(charshanbeHa.getNotificationTimingId());
        ent1.setNotificationGroupEntityList(groupEntityList);
        notificationRepository.save(ent1);

        NotificationEntity ent2 = new NotificationEntity();
        ent2.setTitle("title2");
        ent2.setDescription("desc2");
        ent2.setNotificationTimingId(panjshanbeHa.getNotificationTimingId());
        ent2.setNotificationGroupEntityList(Arrays.asList(group1));
        notificationRepository.save(ent2);
    }
}
