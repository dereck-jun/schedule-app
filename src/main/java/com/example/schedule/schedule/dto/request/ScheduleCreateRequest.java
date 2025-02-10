package com.example.schedule.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleCreateRequest {

    @NotBlank
    @Size(max = 60, message = "제목의 길이는 60을 넘을 수 없습니다.")
    private final String title;

    @NotBlank
    @Size(max = 255, message = "본문의 길이는 255를 넘을 수 없습니다.")
    private final String body;
}
