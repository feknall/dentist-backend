package ir.beheshti.dandun.base.user.entity;

import ir.beheshti.dandun.base.socket.Subscriber;
import ir.beheshti.dandun.base.user.util.SexType;
import ir.beheshti.dandun.base.user.util.UserType;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UserTable")
public class UserEntity implements UserDetails, Subscriber {
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
    private boolean isVerified;

    @Column
    private boolean isSignedUp;

    @Column
    private String verificationCode;

    @Column(columnDefinition = "TEXT")
    private String profilePhoto;

    @Column
    private Timestamp registrationTime;

    @Column
    private String birthPlace;

    @Column
    private String job;

    @Column
    private String address;

    @Column
    private String telephone;

    @Column
    private String fatherName;

    @Enumerated(EnumType.STRING)
    private SexType sex;

    @Column
    private String educationLevel;

    @Column
    private String marriageStatus;

    @Column
    private Timestamp birthDate;

    @Column
    private String notificationToken;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "UserId"), inverseJoinColumns = @JoinColumn(name = "RoleId"))
    private List<RoleEntity> roleEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<UserMultipleChoiceQuestionAnswerEntity> userMultipleChoiceQuestionAnswerEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<UserOpenQuestionAnswerEntity> userOpenQuestionAnswerEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<UserSingleQuestionAnswerEntity> userSingleQuestionAnswerEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<UserImageQuestionAnswerEntity> userImageQuestionAnswerEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<UserRangeQuestionAnswerEntity> userRangeQuestionAnswerEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<UserOpenNumberQuestionAnswerEntity> userOpenNumberQuestionAnswerEntityList;

    @Transient
    private WebSocketSession session;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public WebSocketSession getSession() {
        return session;
    }
}
