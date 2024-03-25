package com.example.demo11.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "Team")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;

    @Column(name = "teamname")
    private String teamName;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Builder
    public Team(String uuid, String teamName, User user) {
        this.teamName = teamName;
        this.user = user;
        this.uuid = uuid;
    }

    public Team() {

    }
}
