package ir.beheshti.dandun.base.user.entity;


import ir.beheshti.dandun.base.user.util.PatientStateType;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "PatientUser")
public class PatientUserEntity {

    @Id
    @Column(name = "PatientId")
    private int patientId;

    @Column
    private PatientStateType patientStateType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PatientId", referencedColumnName = "UserId")
    private UserEntity userEntity;
}
