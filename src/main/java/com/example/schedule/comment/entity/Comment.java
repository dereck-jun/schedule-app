package com.example.schedule.comment.entity;

import com.example.schedule.global.common.entity.BaseTimeEntity;
import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update comments set deleted_date_time = current_timestamp where comment_id = ?")
@SQLRestriction("deleted_date_time is null")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Setter
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Builder
    public Comment(String content, User user, Schedule schedule) {
        this.content = content;
        this.user = user;
        this.schedule = schedule;
    }

    public static Comment of(String content, User user, Schedule schedule) {
        return Comment.builder()
            .content(content)
            .user(user)
            .schedule(schedule)
            .build();
    }
}
