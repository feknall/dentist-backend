package ir.beheshti.dandun.base.user.entity;

import ir.beheshti.dandun.base.user.util.DoctorStateType;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "DoctorUser")
public class DoctorUserEntity {

    @Id
    @Column(name = "DoctorId")
    private int doctorId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DoctorId", referencedColumnName = "UserId")
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    private DoctorStateType doctorStateType;
}