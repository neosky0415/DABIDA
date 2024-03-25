package com.example.demo11.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class TestScore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    private int month01;
    private int month02;
    private int month03;
    private int month04;
    private int month05;
    private int month06;
    private int month07;
    private int month08;
    private int month09;
    private int month10;
    private int month11;
    private int month12;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "testNumber_uuid")
    private TestNumber testNumber;

    public TestScore() {

    }


}