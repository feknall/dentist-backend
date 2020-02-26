package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "UserMultipleChoiceQuestionAnswer")
public class UserMultipleChoiceQuestionAnswerEntity {

    @Id
    @Column(name = "UserMultipleChoiceQuestionAnswerId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "MultipleChoiceQuestionAnswerId")
    private int multipleChoiceQuestionAnswerId;

    @Column(name = "UserId")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "MultipleChoiceQuestionAnswerId", referencedColumnName = "MultipleChoiceQuestionAnswerId", insertable = false, updatable = false)
    private MultipleChoiceQuestionAnswerEntity multipleChoiceQuestionAnswerEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", referencedColumnName = "UserId", insertable = false, updatable = false)
    private UserEntity userEntity;
}
