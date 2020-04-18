package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Data
public class ChatEntity {
    @Id
    @Column(name = "ChatId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int chatId;

    private Integer patientId;
    private Integer doctorId;
    private Integer messageId;
    private boolean active = true;

    @OneToMany(mappedBy = "chatEntity")
    private List<MessageEntity> messageEntityList;
}
