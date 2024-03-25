package com.example.demo11.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubjectForm {
    private String uuid;
    @NotBlank(message = "필수 입력")
    private String subject;

    public SubjectForm() {

    }


}
