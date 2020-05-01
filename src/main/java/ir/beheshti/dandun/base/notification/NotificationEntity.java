package ir.beheshti.dandun.base.notification;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "NotificatoinTable")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int notificationId;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String image;
}
