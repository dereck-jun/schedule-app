package com.example.schedule.schedule.dto.response;

import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.user.dto.response.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ScheduleDto {

    private final Long scheduleId;
    private final String title;
    private final String body;
    private final UserDto userDto;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime updatedDateTime;

    public static ScheduleDto from(Schedule schedule) {
        return new ScheduleDto(
            schedule.getId(),
            schedule.getTitle(),
            schedule.getBody(),
            UserDto.from(schedule.getUser()),
            schedule.getCreatedDateTime(),
            schedule.getUpdatedDateTime()
        );
    }
}
