package com.example.demo11.form;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestedForm {
    private String uuid;
    private String title;
    private String imgPath;

    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;

    private String choice1Img;
    private String choice2Img;
    private String choice3Img;
    private String choice4Img;

    private String correctAnswer;
    private int answer;


    @Builder

    public TestedForm(String uuid, String title, String imgPath, String choice1, String choice2, String choice3, String choice4, String choice1Img, String choice2Img, String choice3Img, String choice4Img, String correctAnswer, int answer) {
        this.uuid = uuid;
        this.title = title;
        this.imgPath = imgPath;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.choice1Img = choice1Img;
        this.choice2Img = choice2Img;
        this.choice3Img = choice3Img;
        this.choice4Img = choice4Img;
        this.correctAnswer = correctAnswer;
        this.answer = answer;
    }
}

