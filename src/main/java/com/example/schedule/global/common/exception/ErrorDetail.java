package com.example.schedule.global.common.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorDetail {

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "code")
    private ErrorCode errorCode;

    private String field;

    private String message;

    public ErrorDetail(String code, String field, String message) {
        this.code = code;
        this.field = field;
        this.message = message;
    }

    public ErrorDetail(ErrorCode errorCode, String field, String message) {
        this.errorCode = errorCode;
        this.field = field;
        this.message = message;
    }

    @JsonProperty("code")
    public String getSerializedCode() {
        return code != null
            ? code : (errorCode != null ? errorCode.name() : null);
    }
}
