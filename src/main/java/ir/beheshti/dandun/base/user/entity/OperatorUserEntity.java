package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "OperatorUser")
public class OperatorUserEntity extends UserEntity {
}