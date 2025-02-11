package com.example.schedule.comment.service;

import com.example.schedule.comment.dto.request.CommentCreateRequest;
import com.example.schedule.comment.dto.request.CommentUpdateRequest;
import com.example.schedule.comment.dto.response.CommentDto;
import com.example.schedule.comment.entity.Comment;
import com.example.schedule.comment.exception.CommentAccessDeniedException;
import com.example.schedule.comment.exception.CommentNotFoundException;
import com.example.schedule.comment.repository.CommentRepository;
import com.example.schedule.global.common.exception.ErrorDetail;
import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.schedule.service.ScheduleService;
import com.example.schedule.user.dto.response.UserDto;
import com.example.schedule.user.entity.User;
import com.example.schedule.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.schedule.global.common.exception.ErrorCode.FORBIDDEN;
import static com.example.schedule.global.common.exception.ErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ScheduleService scheduleService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    /* 댓글 생성 */
    @Transactional
    public CommentDto createComment(Long scheduleId, CommentCreateRequest request, UserDto sessionUserDto) {
        User findUser = userService.findUserByIdOrThrow(sessionUserDto.getUserId());
        Schedule findSchedule = scheduleService.findScheduleByIdOrThrow(scheduleId);
        Comment newComment = commentRepository.save(Comment.of(request.getContent(), findUser, findSchedule));
        return CommentDto.from(newComment);
    }

    /* 댓글 전체 조회 */
    @Transactional(readOnly = true)
    public List<CommentDto> findAllComments(Long scheduleId) {
        Schedule findSchedule = scheduleService.findScheduleByIdOrThrow(scheduleId);
        List<Comment> findComments = commentRepository.findByScheduleId(findSchedule.getId());
        return findComments.stream()
            .map(CommentDto::from)
            .toList();
    }

    /* 댓글 수정 */
    @Transactional
    public CommentDto updateComment(Long scheduleId, Long commentId, CommentUpdateRequest request, UserDto sessionUserDto) {
        scheduleService.findScheduleByIdOrThrow(scheduleId);
        Comment findComment = findCommentOrThrow(commentId);
        checkAuthorization(findComment, sessionUserDto);
        findComment.setContent(request.getContent());
        return CommentDto.from(findComment);
    }

    /* 댓글 삭제 */
    @Transactional
    public void deleteComment(Long scheduleId, Long commentId, UserDto sessionUserDto) {
        scheduleService.findScheduleByIdOrThrow(scheduleId);
        Comment findComment = findCommentOrThrow(commentId);
        checkAuthorization(findComment, sessionUserDto);
        commentRepository.delete(findComment);
    }

    private void checkAuthorization(Comment findComment, UserDto sessionUserDto) {
        if (!findComment.getUser().getId().equals(sessionUserDto.getUserId())) {
            throw new CommentAccessDeniedException(List.of(
                new ErrorDetail(FORBIDDEN, null, "해당 기능을 수행하기 위한 권한이 없습니다.")
            ));
        }
    }

    private Comment findCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException(List.of(
                new ErrorDetail(NOT_FOUND, null, "요청에 해당하는 댓글을 찾을 수 없습니다.")
            )));
    }
}
