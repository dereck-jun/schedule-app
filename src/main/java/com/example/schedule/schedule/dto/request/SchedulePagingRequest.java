package com.example.schedule.schedule.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class SchedulePagingRequest {

    @Min(value = 0, message = "page 값은 0보다 작을 수 없습니다.")
    private final Integer page;

    @Positive(message = "size 값은 0을 포함한 음수일 수 없습니다.")
    private final Integer size;

    public SchedulePagingRequest(Integer page, Integer size) {
        this.page = page == null ? 0 : page;
        this.size = size == null ? 10 : size;
    }
}
