package com.codeit.sb01_deokhugam.domain.comment.dto;

import java.time.Instant;
import java.util.List;

public record CommentResponse(
        List<CommentDto> content,
        String nextCursor,
        Instant nextAfter,
        int size,
        long totalElements,
        boolean hasNext
) {}
