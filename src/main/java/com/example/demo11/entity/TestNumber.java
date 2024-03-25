package com.example.demo11.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@OnDelete(action = OnDeleteAction.CASCADE)
public class TestNumber extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(name = "t_number")
    private String testNumber;

    private String testTitle;


    @OneToMany(mappedBy = "testNumber", orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "testNumber", orphanRemoval = true)
    private List<Tested> testeds = new ArrayList<>();

    @OneToMany(mappedBy = "testNumber", orphanRemoval = true)
    private List<TestStudents> testStudents = new ArrayList<>();

    @OneToMany(mappedBy = "testNumber", orphanRemoval = true)
    private List<TestedInfo> testedInfos = new ArrayList<>();

    @OneToMany(mappedBy = "testNumber", orphanRemoval = true)
    private List<TestScore> testScores = new ArrayList<>();

    @OneToMany(mappedBy = "testNumber", orphanRemoval = true)
    private List<MiniTestStudents> miniTestStudents = new ArrayList<>();

    @OneToMany(mappedBy = "testNumber", orphanRemoval = true)
    private List<MiniTestScore> miniTestScores = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private String miniTitle;
    private Date miniDate;

    public TestNumber() {

    }
    @Builder

    public TestNumber(String uuid, String testNumber, String testTitle, User user, Team team, Subject subject, String miniTitle, Date miniDate) {
        this.uuid = uuid;
        this.testNumber = testNumber;
        this.testTitle = testTitle;
        this.user = user;
        this.team = team;
        this.subject = subject;
        this.miniTitle = miniTitle;
        this.miniDate = miniDate;
    }
}