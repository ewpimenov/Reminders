package com.example.reminder.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@Data
@Table(name = "reminder")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "remind")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date remind;

    @Column(name = "user_id")
    private Long userId;
}
