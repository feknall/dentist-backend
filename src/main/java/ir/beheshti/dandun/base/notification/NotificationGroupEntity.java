package ir.beheshti.dandun.base.notification;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "NotificationGroup")
public class NotificationGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notificationGroupId")
    private int notificationGroupId;

    @Column
    private String name;

    @ManyToMany(mappedBy = "notificationGroupEntityList")
    List<NotificationEntity> notificationEntityList;
}
