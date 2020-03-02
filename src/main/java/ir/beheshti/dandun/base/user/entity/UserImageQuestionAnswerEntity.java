package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "UserImageQuestionAnswer")
public class UserImageQuestionAnswerEntity {

    @Id
    @Column(name = "UserImageQuestionAnswerId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "Image")
    private Byte[] image;

    @Column(name = "EssentialQuestionId")
    private int essentialQuestionId;

    @Column(name = "UserId")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "UserImageQuestionAnswerId", referencedColumnName = "EssentialQuestionId",
            insertable = false, updatable = false)
    private EssentialQuestionEntity essentialQuestionEntity;

    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "UserId",
            insertable = false, updatable = false)
    private UserEntity userEntity;
}
