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
@Table(name = "subject")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Subject extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;

    @Column(name = "subject")
    private String subject;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

        @Builder
    public Subject(String subject, User user) {
        this.subject = subject;
        this.user = user;
    }

    public Subject() {

    }
}
