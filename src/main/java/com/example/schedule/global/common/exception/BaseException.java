package com.example.schedule.global.common.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BaseException extends RuntimeException {

    private final List<ErrorDetail> errorDetails;

    public BaseException(List<ErrorDetail> errorDetails) {
        this.errorDetails = errorDetails;
    }

}
