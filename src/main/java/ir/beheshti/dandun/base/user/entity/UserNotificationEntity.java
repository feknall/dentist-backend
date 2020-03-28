package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "UserNotification")
public class UserNotificationEntity {

    @Id
    @Column(name = "UserNotificationId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userNotificationId;

    @Column(name = "UserId")
    private int userId;

    @Column(name = "NotificationId")
    private int notificationId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "NotificationId", referencedColumnName = "NotificationId", insertable = false, updatable = false)
    private NotificationEntity notificationEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "UserId", referencedColumnName = "UserId", insertable = false, updatable = false)
    private UserEntity userEntity;
}
