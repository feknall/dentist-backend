package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Notification")
public class NotificationEntity {
    @Id
    @Column(name = "NotificationId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int notificationId;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String image;
}
