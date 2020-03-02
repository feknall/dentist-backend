package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "UserRangeQuestionAnswer")
public class UserRangeQuestionAnswerEntity {

    @Id
    @Column(name = "UserRangeQuestionAnswerId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "EssentialQuestionId")
    private int essentialQuestionId;

    @Column(name = "Value")
    private int value;

    @Column(name = "UserId")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "EssentialQuestionId", referencedColumnName = "EssentialQuestionId", insertable = false, updatable = false)
    private EssentialQuestionEntity essentialQuestionEntity;

    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "UserId", insertable = false, updatable = false)
    private UserEntity userEntity;
}
