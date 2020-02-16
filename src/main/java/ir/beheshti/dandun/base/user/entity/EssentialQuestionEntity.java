package ir.beheshti.dandun.base.user.entity;

import ir.beheshti.dandun.base.user.util.QuestionType;
import lombok.Data;

import javax.persistence.*;

@Data
@Table
@Entity
public class EssentialQuestionEntity {

    @Id
    @Column(name = "EssentialQuestionId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String description;

    @Column
    private QuestionType questionType;
}
