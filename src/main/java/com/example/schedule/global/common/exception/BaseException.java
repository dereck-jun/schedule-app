package com.example.schedule.global.common.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BaseException extends RuntimeException {

    private List<ErrorDetail> errorDetail;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(List<ErrorDetail> errorDetail) {
        this.errorDetail = errorDetail;
    }

}
