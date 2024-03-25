package com.example.demo11.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
public class MiniTestStudents extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "testNumber_id")
  private TestNumber testNumber;

  @ManyToOne
  @JoinColumn(name = "testedInfo_id")
  private TestedInfo testedInfo;

}
