package com.example.schedule.user.exception;

import com.example.schedule.global.common.exception.BaseException;
import com.example.schedule.global.common.exception.ErrorDetail;

import java.util.List;

public class UserUnAuthorizeException extends BaseException {

    public UserUnAuthorizeException(List<ErrorDetail> errorDetail) {
        super(errorDetail);
    }
}
