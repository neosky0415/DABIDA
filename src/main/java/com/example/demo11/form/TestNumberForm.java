package com.example.demo11.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TestNumberForm {

    private String uuid;
    private String testNumber;

    private String testTitle;
    private String testSubject;
    private String teamName;
    private String miniTitle;
    private String miniDate;

    public TestNumberForm() {

    }
}
