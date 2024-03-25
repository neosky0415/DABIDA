package com.example.demo11.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class Test {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(name="t_uuid")
    private String testedUuid;

    private int answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="q_uuid")
    private Question question;

}
