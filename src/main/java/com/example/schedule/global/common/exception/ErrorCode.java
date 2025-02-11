package com.example.schedule.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_INPUT("InvalidInput"),
    MISMATCH("Mismatch"),
    LOGIN_FAILED("LoginFailed"),
    UN_AUTHORIZED("UnAuthorized"),
    FORBIDDEN("Forbidden"),
    NOT_FOUND("NotFound"),
    NOT_SUPPORTED("NotSupported"),
    DUPLICATED("Duplicated"),

    SERVER_NOT_WORK("ServerNotWork");

    private final String code;
}
