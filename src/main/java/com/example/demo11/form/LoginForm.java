package com.example.demo11.form;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginForm {
    @Email(message="ID가 일치하지 않습니다.")
    private String username;
    private String password;
}
