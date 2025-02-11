package com.example.schedule.comment.dto.response;

import com.example.schedule.comment.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentDto {

    private final Long commentId;
    private final String content;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime updatedDateTime;
    private final String username;
    private final Long scheduleId;

    public static CommentDto from(Comment comment) {
        return new CommentDto(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedDateTime(),
            comment.getUpdatedDateTime(),
            comment.getUser().getUsername(),
            comment.getSchedule().getId()
        );
    }
}
