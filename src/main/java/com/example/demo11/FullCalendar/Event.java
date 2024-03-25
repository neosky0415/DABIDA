package com.example.demo11.FullCalendar;

import com.example.demo11.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@OnDelete(action = OnDeleteAction.CASCADE)
@NoArgsConstructor
@Data
@Entity
public class Event {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "start")
    private String start;
    @Column(name = "end")
    private String end;

    @Column(name = "title", length = 200)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



    @Builder

    public Event(int id, String start, String end, String title, User user) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.title = title;
        this.user = user;
    }
}
