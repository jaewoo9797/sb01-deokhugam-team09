package com.codeit.sb01_deokhugam.domain.comment.repository;

import com.codeit.sb01_deokhugam.domain.comment.entity.Comment;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CommentRepositoryCustom {
    List<Comment> findComments(UUID reviewId, Instant after, Instant before, boolean isAsc, int limit);
}
