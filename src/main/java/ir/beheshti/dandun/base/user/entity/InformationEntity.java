package ir.beheshti.dandun.base.user.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Information")
public class InformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int informationId;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String image;

}
