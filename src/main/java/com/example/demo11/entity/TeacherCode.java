package com.example.demo11.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
public class TeacherCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teacherCode;

    public TeacherCode() {

    }

    @Builder
    public TeacherCode(Long id, String teacherCode) {
        this.id = id;
        this.teacherCode = teacherCode;
    }
}
