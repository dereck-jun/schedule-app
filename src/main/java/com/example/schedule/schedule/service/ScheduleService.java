package com.example.schedule.schedule.service;

import com.example.schedule.global.common.exception.ErrorDetail;
import com.example.schedule.schedule.dto.request.ScheduleCreateRequest;
import com.example.schedule.schedule.dto.request.SchedulePagingRequest;
import com.example.schedule.schedule.dto.request.ScheduleUpdateRequest;
import com.example.schedule.schedule.dto.response.ScheduleDto;
import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.schedule.exception.ScheduleNotFoundException;
import com.example.schedule.schedule.exception.ScheduleUpdateException;
import com.example.schedule.schedule.repository.ScheduleRepository;
import com.example.schedule.user.dto.response.UserDto;
import com.example.schedule.user.entity.User;
import com.example.schedule.user.exception.UserUnAuthorizeException;
import com.example.schedule.user.exception.UserUpdateException;
import com.example.schedule.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.schedule.global.common.exception.ErrorCode.*;
import static com.example.schedule.global.common.exception.ErrorCode.INVALID_INPUT;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserService userService;

    /* 일정 생성 */
    @Transactional
    public ScheduleDto createSchedule(ScheduleCreateRequest request, UserDto sessionUserDto) {
        User findUser = userService.getUserOrThrow(sessionUserDto.getUserId());
        Schedule newSchedule = saveSchedule(request, findUser);
        return ScheduleDto.from(newSchedule);
    }

    /* 일정 단건 조회 */
    @Transactional(readOnly = true)
    public ScheduleDto findSchedule(Long scheduleId) {
        Schedule findSchedule = getScheduleOrThrow(scheduleId);
        return ScheduleDto.from(findSchedule);
    }

    /* 일정 전체 조회 (페이징 적용) */
    @Transactional(readOnly = true)
    public Page<ScheduleDto> findAllOrderByUpdatedDateTime(SchedulePagingRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "updatedDateTime"));
        Page<Schedule> page = scheduleRepository.findAllByUpdatedDateDesc(pageRequest);
        return page.map(ScheduleDto::from);
    }

    /* 일정 수정 */
    @Transactional
    public ScheduleDto updateSchedule(Long scheduleId, ScheduleUpdateRequest request, UserDto sessionUserDto) {
        if (request.getTitle() == null && request.getBody() == null) {
            throw new ScheduleUpdateException(List.of(
                new ErrorDetail(INVALID_INPUT, "title", "모든 필드가 비어있을 수 없습니다."),
                new ErrorDetail(INVALID_INPUT, "body", "모든 필드가 비어있을 수 없습니다.")
            ));
        }
        Schedule findSchedule = getScheduleOrThrow(scheduleId);
        checkAuthorization(findSchedule, sessionUserDto);
        updateSchedule(request, findSchedule);
        return ScheduleDto.from(findSchedule);
    }

    /* 일정 삭제 */
    @Transactional
    public void deleteSchedule(Long scheduleId, UserDto sessionUserDto) {
        Schedule findSchedule = getScheduleOrThrow(scheduleId);
        checkAuthorization(findSchedule, sessionUserDto);
        scheduleRepository.delete(findSchedule);
    }

    /* 타 도메인에서 Entity 자체를 필요로 하기 때문에 public 제한자로 생성 */
    public Schedule getScheduleOrThrow(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(List.of(
                new ErrorDetail(NOT_FOUND, null, "요청에 해당하는 일정을 찾을 수 없습니다.")
            )));
    }

    private void checkAuthorization(Schedule schedule, UserDto sessionUserDto) {
        if (!schedule.getUser().getId().equals(sessionUserDto.getUserId())) {
            throw new UserUnAuthorizeException(List.of(
                new ErrorDetail(UN_AUTHORIZED, null, "해당 기능을 수행하기 위한 권한이 없습니다.")
            ));
        }
    }

    private Schedule saveSchedule(ScheduleCreateRequest request, User findUser) {
        return scheduleRepository.save(
            Schedule.of(request.getTitle(), request.getBody(), findUser));
    }

    private void updateSchedule(ScheduleUpdateRequest request, Schedule findSchedule) {
        if (request.getTitle() != null) {
            if (findSchedule.getTitle().equals(request.getTitle())) {
                throw new UserUpdateException(List.of(
                    new ErrorDetail(INVALID_INPUT, "title", "이전 제목과 동일할 수 없습니다.")
                ));
            }
            findSchedule.setTitle(request.getTitle());
        }

        if (request.getBody() != null) {
            if (findSchedule.getBody().equals(request.getBody())) {
                throw new UserUpdateException(List.of(
                    new ErrorDetail(INVALID_INPUT, "body", "이전 내용과 동일할 수 없습니다.")
                ));
            }
            findSchedule.setBody(request.getBody());
        }
    }
}
