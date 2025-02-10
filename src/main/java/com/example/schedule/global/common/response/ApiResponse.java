package com.example.schedule.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ApiResponse<T> {

    private HttpStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    private String message;

    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(HttpStatus status, T data, String message) {
        return new ApiResponse<>(status, data, message, LocalDateTime.now());
    }
}
