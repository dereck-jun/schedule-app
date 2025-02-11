package com.example.schedule.schedule.entity;

import com.example.schedule.comment.entity.Comment;
import com.example.schedule.global.common.entity.BaseTimeEntity;
import com.example.schedule.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "schedules")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update schedules set deleted_date_time = current_timestamp where schedule_id = ?")
@SQLRestriction(value = "deleted_date_time is null")
public class Schedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(nullable = false)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @Builder
    public Schedule(String title, String body, User user) {
        this.title = title;
        this.body = body;
        this.user = user;
    }

    public static Schedule of(String title, String body, User user) {
        return Schedule.builder()
            .title(title)
            .body(body)
            .user(user)
            .build();
    }
}
