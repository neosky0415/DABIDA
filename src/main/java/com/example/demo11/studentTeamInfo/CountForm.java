package com.example.demo11.studentTeamInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CountForm {

    public String uuid;
    public String teamName;
    public int count;
    public Date createdAt;
    public Date editedAt;




}
