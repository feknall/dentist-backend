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
        NotificationTimingEntity first = new NotificationTimingEntity();
        first.setNotificationTimingId(1);
        first.setName("شنبه‌ها ساعت ۱۴");
        notificationTimingRepository.save(first);

        NotificationTimingEntity second = new NotificationTimingEntity();
        second.setNotificationTimingId(2);
        second.setName("شنبه‌ها ساعت ۲۰");
        notificationTimingRepository.save(second);

        NotificationTimingEntity third = new NotificationTimingEntity();
        third.setNotificationTimingId(3);
        third.setName("جمعه‌ها ساعت ۱۴");
        notificationTimingRepository.save(third);

        NotificationTimingEntity fourth = new NotificationTimingEntity();
        fourth.setNotificationTimingId(4);
        fourth.setName("جمعه‌ها ساعت ۲۰");

        notificationTimingRepository.save(fourth);

        NotificationGroupEntity green = new NotificationGroupEntity();
        green.setName(NotificationGroupType.GREEN.getValue());

        NotificationGroupEntity red = new NotificationGroupEntity();
        red.setName(NotificationGroupType.RED.getValue());

        NotificationGroupEntity yellow = new NotificationGroupEntity();
        yellow.setName(NotificationGroupType.YELLOW.getValue());

        NotificationGroupEntity doctor = new NotificationGroupEntity();
        doctor.setName(NotificationGroupType.DOCTOR.getValue());

        List<NotificationGroupEntity> groupEntityList =
                Arrays.asList(green, red, yellow, doctor);

        notificationGroupRepository.saveAll(groupEntityList);
    }
}
