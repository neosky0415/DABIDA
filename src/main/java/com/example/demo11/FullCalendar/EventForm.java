package com.example.demo11.FullCalendar;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EventForm {
    private int id;

    private String title;

    private String start;

    private String end;
}
