package com.example.demo11.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class Tested extends BaseEntity1{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  String uuid;

    private  String testedUuid;

    private int answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="q_uuid")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "testNumber_uuid")
    private TestNumber testNumber;

    @Builder
    public Tested(String uuid, String testedUuid, int answer, Question question, User user, TestNumber testNumber) {
        this.uuid = uuid;
        this.testedUuid = testedUuid;
        this.answer = answer;
        this.question = question;
        this.user = user;
        this.testNumber = testNumber;
    }

    public Tested() {
    }
}
