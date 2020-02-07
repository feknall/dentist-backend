package ir.beheshti.dandun.dandun.user.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "PatientUser")
public class PatientUserEntity extends UserEntity {
}
