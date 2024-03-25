package com.example.demo11.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class QuestionForm {

    private  String quuid;

    @NotBlank(message = " *필수 입력")
    private String title;
    @NotNull(message = " *필수 입력")
    private String choice1=" ";
    @NotNull(message = " *필수 입력")
    private String choice2=" ";
    @NotNull(message = " *필수 입력")
    private String choice3=" ";
    @NotNull(message = " *필수 입력")
    private String choice4=" ";
    @Pattern(regexp = "^[1-4]$" , message =" *1~4 숫자 입력" )
    private String correctAnswer;

    private Date createdAt;

    private Date editedAt;



}
