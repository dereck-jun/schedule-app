package com.example.schedule.schedule.exception;

import com.example.schedule.global.common.exception.BaseException;
import com.example.schedule.global.common.exception.ErrorDetail;

import java.util.List;

public class ScheduleNotFoundException extends BaseException {

    public ScheduleNotFoundException(List<ErrorDetail> errorDetail) {
        super(errorDetail);
    }
}
