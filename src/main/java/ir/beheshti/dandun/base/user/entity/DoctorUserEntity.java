package ir.beheshti.dandun.base.user.entity;

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

    @Column
    private boolean isActive;
}