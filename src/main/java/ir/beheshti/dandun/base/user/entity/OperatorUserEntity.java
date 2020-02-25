package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "OperatorUser")
public class OperatorUserEntity {

    @Id
    @Column(name = "OperatorId")
    private int operatorId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "OperatorId", referencedColumnName = "UserId")
    private UserEntity userEntity;
}