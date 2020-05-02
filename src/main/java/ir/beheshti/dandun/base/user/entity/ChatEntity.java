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

    @Column(name = "PatientId")
    private Integer patientId;
    @Column(name = "DoctorId")
    private Integer doctorId;

    @OneToMany(mappedBy = "chatEntity")
    private List<MessageEntity> messageEntityList;

    @ManyToOne
    @JoinColumn(name = "PatientId", referencedColumnName = "UserId")
    private UserEntity patientEntity;

    @ManyToOne
    @JoinColumn(name = "DoctorId", referencedColumnName = "UserId")
    private UserEntity doctorEntity;
}
