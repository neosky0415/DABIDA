package com.example.demo11.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@Table(name="question")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Question extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name="quuid",length = 38)
    private String quuid;

    private String title;
    private String imgPath; // 이미지 조회 경로

    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String username; //ID
    private String nickname; // 진짜 이름?

    //이미지 경로 DB 저장
    private String choice1Img;
    private String choice2Img;
    private String choice3Img;
    private String choice4Img;

    @Column(name = "correct_answer")
    private  String correctAnswer;

    @OnDelete(action= OnDeleteAction.CASCADE)
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "testNumber_uuid")
    private TestNumber testNumber;

    public Question() {

    }


    }





