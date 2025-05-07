package com.codeit.sb01_deokhugam.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CommentCreateRequest (
        @NotNull(message = "리뷰 ID는 필수 입력 항목입니다.")
        UUID reviewId,

        @NotNull(message = "사용자 ID는 필수 입력 항목입니다.")
        UUID userId,

        @NotBlank(message = "댓글 내용은 필수 입력 항목입니다.")
        String content
) {}
