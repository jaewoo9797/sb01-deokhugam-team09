package com.codeit.sb01_deokhugam.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
        @NotBlank(message = "댓글 내용은 필수 입력 항목입니다.")
        String content
) {}
