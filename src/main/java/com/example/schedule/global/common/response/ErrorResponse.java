package com.example.schedule.global.common.response;

import com.example.schedule.global.common.exception.ErrorDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private HttpStatus status;
    private List<ErrorDetail> errorDetails;

    private LocalDateTime timestamp;

    public static ErrorResponse fail(HttpStatus status, List<ErrorDetail> errorDetails) {
        return new ErrorResponse(status, errorDetails, LocalDateTime.now());
    }
}
