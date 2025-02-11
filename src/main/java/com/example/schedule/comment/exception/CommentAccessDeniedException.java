package com.example.schedule.comment.exception;

import com.example.schedule.global.common.exception.BaseException;
import com.example.schedule.global.common.exception.ErrorDetail;

import java.util.List;

public class CommentAccessDeniedException extends BaseException {

    public CommentAccessDeniedException(List<ErrorDetail> errorDetail) {
        super(errorDetail);
    }
}
