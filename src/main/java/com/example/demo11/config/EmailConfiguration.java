package com.example.demo11.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "spring.mail")
public class EmailConfiguration {

    private String host;
    private String username;
    private String password;
    private int port;
}
