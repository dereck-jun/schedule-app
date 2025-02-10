package com.example.schedule.schedule.controller;

import com.example.schedule.global.common.response.ApiResponse;
import com.example.schedule.schedule.dto.request.ScheduleCreateRequest;
import com.example.schedule.schedule.dto.request.SchedulePagingRequest;
import com.example.schedule.schedule.dto.request.ScheduleUpdateRequest;
import com.example.schedule.schedule.dto.response.ScheduleDto;
import com.example.schedule.schedule.service.ScheduleService;
import com.example.schedule.user.dto.response.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static com.example.schedule.global.common.SessionConst.LOGIN_MEMBER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/api/v1/schedules")
    public ApiResponse<ScheduleDto> createSchedule(@Valid @RequestBody ScheduleCreateRequest createRequest,
                                                   HttpServletRequest request) {
        UserDto getUserDto = findUserDtoBySession(request);
        ScheduleDto newScheduleDto = scheduleService.createSchedule(createRequest, getUserDto);
        return ApiResponse.success(OK, newScheduleDto, "일정 생성 성공");
    }

    @GetMapping("/api/v1/schedules")
    public ApiResponse<Page<ScheduleDto>> findAllSchedules(@Valid @ModelAttribute SchedulePagingRequest request) {
        Page<ScheduleDto> scheduleDtoPage = scheduleService.findAllOrderByUpdatedDateTime(request);
        return ApiResponse.success(OK, scheduleDtoPage, "일정 전체 조회 성공");
    }

    @GetMapping("/api/v1/schedules/{scheduleId}")
    public ApiResponse<ScheduleDto> findSchedule(@PathVariable Long scheduleId) {
        ScheduleDto getScheduleDto = scheduleService.findSchedule(scheduleId);
        return ApiResponse.success(OK, getScheduleDto, "일정 단건 조회 성공");
    }

    @PatchMapping("/api/v1/schedules/{scheduleId}")
    public ApiResponse<ScheduleDto> updateSchedule(@PathVariable Long scheduleId,
                                                   @Valid @RequestBody ScheduleUpdateRequest updateRequest,
                                                   HttpServletRequest request) {
        UserDto getUserDto = findUserDtoBySession(request);
        ScheduleDto getScheduleDto = scheduleService.updateSchedule(scheduleId, updateRequest, getUserDto);
        return ApiResponse.success(OK, getScheduleDto, "일정 수정 성공");
    }

    @DeleteMapping("/api/v1/schedules/{scheduleId}")
    public ApiResponse<Void> deleteSchedule(@PathVariable Long scheduleId, HttpServletRequest request) {
        UserDto getUserDto = findUserDtoBySession(request);
        scheduleService.deleteSchedule(scheduleId, getUserDto);
        return ApiResponse.success(OK, null, "일정 삭제 성공");
    }

    private UserDto findUserDtoBySession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (UserDto) session.getAttribute(LOGIN_MEMBER);
    }
}