package com.example.demo11.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserForm {

    private Long id;
    @Email
    @NotBlank(message = "이름은 Null 일 수 없습니다!")
    private String username;
    private String password;
    private String password2;
    private String realName;

    private String nickname;
    private String phoneNum;
    private String guardianNum;
    private String subject;
    private String teamName;
    private String roles;
    private String teacherCode;

    // OAuth 로그인에 사용
    private String provider;
    private String providerId;
    private String loginId;
    private int loginFailureCount;

    public UserForm(){

    }
}
