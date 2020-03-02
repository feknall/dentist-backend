package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "UserSingleQuestionAnswer")
public class UserSingleQuestionAnswerEntity {

    @Id
    @Column(name = "UserSingleQuestionAnswerId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "EssentialQuestionId")
    private int essentialQuestionId;

    @Column(name = "MultipleChoiceQuestionAnswerId")
    private int multipleChoiceQuestionAnswerId;

    @Column(name = "UserId")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "EssentialQuestionId", referencedColumnName = "EssentialQuestionId", insertable = false, updatable = false)
    private EssentialQuestionEntity essentialQuestionEntity;

    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "UserId", insertable = false, updatable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "MultipleChoiceQuestionAnswerId", referencedColumnName = "MultipleChoiceQuestionAnswerId", insertable = false, updatable = false)
    private MultipleChoiceQuestionAnswerEntity multipleChoiceQuestionAnswerEntity;

}
