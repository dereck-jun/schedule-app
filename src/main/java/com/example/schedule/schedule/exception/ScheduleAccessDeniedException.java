package com.example.schedule.schedule.exception;

import com.example.schedule.global.common.exception.BaseException;
import com.example.schedule.global.common.exception.ErrorDetail;

import java.util.List;

public class ScheduleAccessDeniedException extends BaseException {

    public ScheduleAccessDeniedException(List<ErrorDetail> errorDetail) {
        super(errorDetail);
    }
}
