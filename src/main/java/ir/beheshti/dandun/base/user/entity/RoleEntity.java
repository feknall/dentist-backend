package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "Role")
public class RoleEntity {

    @Id
    @Column(name = "RoleId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String name;


    @ManyToMany(mappedBy = "roleEntityList", fetch = FetchType.LAZY)
    private List<PermissionEntity> permissionEntityList;

    @ManyToMany(mappedBy = "roleEntityList", fetch = FetchType.LAZY)
    private List<UserEntity> userEntityList;
}
