package com.example.schedule.schedule.dto.response;

import com.example.schedule.schedule.entity.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ScheduleWithCountDto {

    private final Long scheduleId;
    private final String title;
    private final String body;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime updatedDateTime;
    private final String username;
    private final int commentCount;

    public static ScheduleWithCountDto from(Schedule schedule) {
        return new ScheduleWithCountDto(
            schedule.getId(),
            schedule.getTitle(),
            schedule.getBody(),
            schedule.getCreatedDateTime(),
            schedule.getUpdatedDateTime(),
            schedule.getUser().getUsername(),
            schedule.getComments().size()
        );
    }
}
