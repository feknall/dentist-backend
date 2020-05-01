package ir.beheshti.dandun.base.faq;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "FaqTable")
public class FaqEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int faqId;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String image;
}
