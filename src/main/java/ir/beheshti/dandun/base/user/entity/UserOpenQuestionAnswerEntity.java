package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "UserOpenQuestionAnswer")
public class UserOpenQuestionAnswerEntity {

    @Id
    @Column(name = "OpenQuestionAnswerId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "EssentialQuestionId")
    private int essentialQuestionId;

    @Column(name = "UserId")
    private int userId;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "EssentialQuestionId", referencedColumnName = "EssentialQuestionId", insertable = false, updatable = false)
    private EssentialQuestionEntity essentialQuestionEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", referencedColumnName = "UserId", insertable = false, updatable = false)
    private UserEntity userEntity;
}
