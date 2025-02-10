package com.example.schedule.comment.controller;

import com.example.schedule.comment.dto.request.CommentCreateRequest;
import com.example.schedule.comment.dto.request.CommentUpdateRequest;
import com.example.schedule.comment.dto.response.CommentDto;
import com.example.schedule.comment.service.CommentService;
import com.example.schedule.global.common.response.ApiResponse;
import com.example.schedule.user.dto.response.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.schedule.global.common.SessionConst.LOGIN_MEMBER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/v1/schedules/{scheduleId}/comments")
    public ApiResponse<CommentDto> createComment(@PathVariable Long scheduleId,
                                                 @Valid @RequestBody CommentCreateRequest createRequest,
                                                 HttpServletRequest request) {
        UserDto getUserDto = findUserDtoBySession(request);
        CommentDto commentDto = commentService.createComment(scheduleId, createRequest, getUserDto);
        return ApiResponse.success(OK, commentDto, "댓글 생성 성공");
    }

    @GetMapping("/api/v1/schedules/{scheduleId}/comments")
    public ApiResponse<List<CommentDto>> findAllComments(@PathVariable Long scheduleId) {
        List<CommentDto> findCommentDtos = commentService.findAllComments(scheduleId);
        return ApiResponse.success(OK, findCommentDtos, "댓글 전체 조회 성공");
    }

    @PatchMapping("/api/v1/schedules/{scheduleId}/comments/{commentId}")
    public ApiResponse<CommentDto> updateComment(@PathVariable Long scheduleId,
                                                    @PathVariable Long commentId,
                                                    @Valid @RequestBody CommentUpdateRequest updateRequest,
                                                    HttpServletRequest request) {
        UserDto getUserDto = findUserDtoBySession(request);
        CommentDto commentDto = commentService.updateComment(scheduleId, commentId, updateRequest, getUserDto);
        return ApiResponse.success(OK, commentDto, "댓글 수정 성공");
    }

    @DeleteMapping("/api/v1/schedules/{scheduleId}/comments/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long scheduleId,
                              @PathVariable Long commentId,
                              HttpServletRequest request) {
        UserDto getUserDto = findUserDtoBySession(request);
        commentService.deleteComment(scheduleId, commentId, getUserDto);
        return ApiResponse.success(OK, null, "댓글 삭제 성공");
    }

    private UserDto findUserDtoBySession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (UserDto) session.getAttribute(LOGIN_MEMBER);
    }
}
