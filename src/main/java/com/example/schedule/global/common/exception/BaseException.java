package com.example.schedule.global.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private ErrorDetail errorDetail;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(ErrorDetail errorDetail) {
        this.errorDetail = errorDetail;
    }
}
