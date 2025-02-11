package com.example.schedule.global.common.exception;

import com.example.schedule.global.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

import static com.example.schedule.global.common.exception.ErrorCode.SERVER_NOT_WORK;
import static jakarta.servlet.http.HttpServletResponse.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.schedule")
public class GlobalControllerAdvice {

    @ExceptionHandler(value = BaseException.class)
    public ErrorResponse baseExceptionHandler(BaseException be, HttpServletResponse response) {
        List<ErrorDetail> errorDetails = new ArrayList<>(be.getErrorDetails());
        HttpStatus status = errorDetails.stream()
            .map(errorDetail -> getCodeAndHttpStatus(response, errorDetail))
            .findFirst()
            .orElse(BAD_REQUEST);
        return ErrorResponse.fail(status, errorDetails);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException manve, HttpServletResponse response) {
        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();

        List<ErrorDetail> errorDetails = fieldErrors.stream()
            .map(fieldError -> {
                String code = fieldError.getCode();
                String field = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                return new ErrorDetail(code, field, message);
            }).toList();

        response.setStatus(SC_BAD_REQUEST);
        return ErrorResponse.fail(BAD_REQUEST, errorDetails);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ErrorResponse methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException matme, HttpServletResponse response) {
        String code = matme.getErrorCode();
        String field = matme.getName();

        String requiredType = (matme.getRequiredType() != null)
            ? matme.getRequiredType().getSimpleName()
            : "Unknown";

        String message = String.format(
            "필드 \"%s\"에 잘못된 타입이 전달되었습니다. (기대: %s, 전달값: %s)",
            field, requiredType, matme.getValue());

        ErrorDetail errorDetail = new ErrorDetail(code, field, message);
        response.setStatus(SC_BAD_REQUEST);
        return ErrorResponse.fail(BAD_REQUEST, List.of(errorDetail));
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse exceptionHandler(Exception e, HttpServletResponse response) {
        log.error("[Exception]: {}", e.getLocalizedMessage());
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
        return ErrorResponse.fail(INTERNAL_SERVER_ERROR, List.of(new ErrorDetail(SERVER_NOT_WORK, null, "서버 문제로 인해 실패했습니다.")));
    }

    private HttpStatus getCodeAndHttpStatus(HttpServletResponse response, ErrorDetail errorDetail) {
        ErrorCode errorCode = errorDetail.getErrorCode();
        HttpStatus status;
        switch (errorCode) {
            case UN_AUTHORIZED, LOGIN_FAILED -> {
                response.setStatus(SC_UNAUTHORIZED);
                status = UNAUTHORIZED;
            }
            case FORBIDDEN -> {
                response.setStatus(SC_FORBIDDEN);
                status = FORBIDDEN;
            }
            case NOT_FOUND -> {
                response.setStatus(SC_NOT_FOUND);
                status = NOT_FOUND;
            }
            case NOT_SUPPORTED -> {
                response.setStatus(SC_METHOD_NOT_ALLOWED);
                status = METHOD_NOT_ALLOWED;
            }
            case DUPLICATED -> {
                response.setStatus(SC_CONFLICT);
                status = CONFLICT;
            }
            case SERVER_NOT_WORK -> {
                response.setStatus(SC_INTERNAL_SERVER_ERROR);
                status = INTERNAL_SERVER_ERROR;
            }
            default -> {
                response.setStatus(SC_BAD_REQUEST);
                status = BAD_REQUEST;
            }
        }
        return status;
    }
}
