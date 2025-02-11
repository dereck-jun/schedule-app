package com.example.schedule.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentUpdateRequest {

    @NotBlank
    @Size(max = 255, message = "댓글 본문의 길이는 255를 넘을 수 없습니다.")
    private final String content;
}
