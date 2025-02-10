package com.example.schedule.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MISMATCH("Mismatch"),
    UN_AUTHORIZED("UnAuthorized"),
    NOT_FOUND("NotFound"),
    NOT_SUPPORTED("NotSupported"),
    DUPLICATED("Duplicated"),

    SERVER_NOT_WORK("ServerNotWork");

    private final String code;
}
