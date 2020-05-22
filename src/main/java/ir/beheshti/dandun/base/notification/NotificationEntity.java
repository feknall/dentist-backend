package ir.beheshti.dandun.base.notification;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "NotificatoinTable")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notificationId")
    private int notificationId;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(name = "notificationTimingId")
    private int notificationTimingId;

    @JoinColumn(name = "notificationTimingId", referencedColumnName = "notificationTimingId",
            insertable = false, updatable = false)
    @ManyToOne
    private NotificationTimingEntity notificationTimingEntity;

    @ManyToMany
    @JoinTable(name = "NotificationNotificationGroup",
            joinColumns = @JoinColumn(name = "notificationGroupId"),
            inverseJoinColumns = @JoinColumn(name = "notificationId"))
    private List<NotificationGroupEntity> notificationGroupEntityList;
}
