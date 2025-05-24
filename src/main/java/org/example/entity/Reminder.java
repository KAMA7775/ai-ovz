package org.example.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
@Data
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String text;

    @Column(name = "remind_at")
    private LocalDateTime remindAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

}

