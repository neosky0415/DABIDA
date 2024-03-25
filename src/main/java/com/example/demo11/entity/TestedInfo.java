package com.example.demo11.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@Data
@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class TestedInfo extends BaseEntity1 {

    @Id
    private String uuid;



    @Column(name="question_count")
    private int questionCount;

    @Column(name="score")
    private int score;

    @Column(name="correct_answer_count")
    private int correctAnswerCount;

    @Column(name="test_result")
    private String testResult;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "testNumber_id")
    private TestNumber testNumber;


    @Builder
    public TestedInfo(String uuid, int questionCount, int score, int correctAnswerCount, String testResult, User user, TestNumber testNumber) {
        this.uuid = uuid;
        this.questionCount = questionCount;
        this.score = score;
        this.correctAnswerCount = correctAnswerCount;
        this.testResult = testResult;
        this.user = user;
        this.testNumber = testNumber;
    }
}

