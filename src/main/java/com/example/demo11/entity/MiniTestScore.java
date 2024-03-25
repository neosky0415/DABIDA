package com.example.demo11.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class MiniTestScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date testedDate;
    private int score;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tested_uuid")
    private Tested tested;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "testNumber_uuid")
    private TestNumber testNumber;

    public MiniTestScore() {

    }

    @Builder
    public MiniTestScore(Long id, int score, Date testedDate, User user, Tested tested, TestNumber testNumber) {
        this.id = id;
        this.testedDate = testedDate;
        this.score = score;
        this.user = user;
        this.tested = tested;
        this.testNumber = testNumber;
    }
}
