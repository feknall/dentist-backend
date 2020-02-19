package ir.beheshti.dandun.base.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MultipleChoiceQuestionAnswer")
public class MultipleChoiceQuestionAnswerEntity {

    @Id
    @Column(name = "MultipleChoiceQuestionAnswerId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "EssentialQuestionId")
    private Integer essentialQuestionId;

    @Column
    private String description;

    @Column
    private Integer choiceOrder;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EssentialQuestionId", referencedColumnName = "EssentialQuestionId", insertable = false, updatable = false)
    private EssentialQuestionEntity essentialQuestionEntity;
}
