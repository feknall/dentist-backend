package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "Permission")
public class PermissionEntity {

    @Id
    @Column(name = "PermissionId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PermissionRole", joinColumns = @JoinColumn(name = "PermissionId"), inverseJoinColumns = @JoinColumn(name = "RoleId"))
    private List<RoleEntity> roleEntityList;
}
