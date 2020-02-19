package ir.beheshti.dandun.base.user.entity;

import ir.beheshti.dandun.base.user.util.QuestionType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "EssentialQuestion")
@Entity
public class EssentialQuestionEntity {

    @Id
    @Column(name = "EssentialQuestionId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column
    private Integer questionOrder;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "essentialQuestionEntity")
    private List<MultipleChoiceQuestionAnswerEntity> multipleChoiceQuestionAnswerEntityList;
}
