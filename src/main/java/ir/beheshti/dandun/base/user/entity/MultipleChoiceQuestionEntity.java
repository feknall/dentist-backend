package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class MultipleChoiceQuestionEntity {

    @Id
    @Column(name = "MultipleChoiceQuestionId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "EssentialQuestionId")
    private Integer essentialQuestionId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EssentialQuestionId", referencedColumnName = "EssentialQuestionId")
    private EssentialQuestionEntity essentialQuestionEntity;
}
