package ir.beheshti.dandun.dandun.user.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UserTable")
public class UserEntity {

    @Id
    @Column(name = "UserId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String nationalCode;

    @Column
    private String password;

    @Column
    private String phoneNumber;

    @Column
    private boolean isValidated;

    @Column
    private Byte[] profilePhoto;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "UserId"), inverseJoinColumns = @JoinColumn(name = "RoleId"))
    private List<RoleEntity> roleEntityList;
}
