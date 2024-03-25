package com.example.demo11.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeacherCodeForm {

    private Long id;

    private String teacherCode;

    public TeacherCodeForm() {

    }
}
