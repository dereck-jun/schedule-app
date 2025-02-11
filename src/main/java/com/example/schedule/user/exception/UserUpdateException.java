package com.example.schedule.user.exception;

import com.example.schedule.global.common.exception.BaseException;
import com.example.schedule.global.common.exception.ErrorDetail;

import java.util.List;

public class UserUpdateException extends BaseException {

    public UserUpdateException(List<ErrorDetail> errorDetail) {
        super(errorDetail);
    }
}
