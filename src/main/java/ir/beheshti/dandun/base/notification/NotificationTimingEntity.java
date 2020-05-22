package ir.beheshti.dandun.base.notification;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "NotificatoinTimingTable")
public class NotificationTimingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notificationTimingId")
    private int notificationTimingId;

    @Column
    private String name;
    @Column
    private String cron;
}
