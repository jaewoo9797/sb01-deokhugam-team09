package com.codeit.sb01_deokhugam.domain.comment.dto;

import java.util.UUID;

public record CommentCreateRequest (
        UUID reviewID,
        UUID userId,
        String content
) {}
