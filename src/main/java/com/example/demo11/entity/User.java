package com.example.demo11.entity;


import com.example.demo11.form.UserForm;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="user_table")
@SQLDelete(sql = "UPDATE user_table SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@Where(clause = "deleted_at is null")

public class User extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name = "username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="nickname")
    private String nickname;
    @Column(name="real_name")
    private String realName;
    @Column(name = "phone_number")
    private String phoneNum;
    @Column(name = "guardian_number")
    private String guardianNum;
    @Column(name = "subject")
    private String subject;
    @Column(name = "teamname")
    private String teamName;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Team> teams = new ArrayList<>();

    private String roles;


    private String userImgPath; //DB 이미지 경로

    // OAuth 로그인에 사용
    private String loginId;
    private String provider;
    private String providerId;
    //로그인 실패 횟수
    @Column(name="login_failure_count")
    private int loginFailureCount = 0;

    @Column(nullable = true , unique = false , updatable = true)
    private LocalDateTime deletedAt;

    public User() {

    }
    @Builder
    public User(Long id, String username, String password, String nickname, String realName, String phoneNum,
                String guardianNum, String subject, String teamName, String roles, String userImgPath,
                String loginId, String provider, String providerId, int loginFailureCount, LocalDateTime deletedAt
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.realName = realName;
        this.phoneNum = phoneNum;
        this.guardianNum = guardianNum;
        this.subject = subject;
        this.teamName = teamName;
        this.roles = roles;
        this.userImgPath = userImgPath;
        this.loginId = loginId;
        this.provider = provider;
        this.providerId = providerId;
        this.loginFailureCount = loginFailureCount;
        this.deletedAt = deletedAt;
    }


    public static User toUserEntity(
            UserForm userForm
    ) {
        User user = new User();
        return user;
    }


}


