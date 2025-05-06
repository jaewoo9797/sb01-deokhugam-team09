package com.codeit.sb01_deokhugam.domain.comment.repository;

import com.codeit.sb01_deokhugam.domain.comment.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository <Comment, UUID> {

    List<Comment> findByReviewIdAndDeletedFalseAndCreatedAtAfterAndCreatedAtBefore(
            UUID reviewId, Instant after, Instant before, Sort sort);

    // 삭제되지 않은 댓글을 ID로 조회
    Optional<Comment> findByIdAndDeletedFalse(UUID commentId);

    // 작성자가 본인인지 확인할 수 있도록
    Optional<Comment> findByIdAndUserIdAndDeletedFalse(UUID commentId, UUID userId);

    // 물리 삭제
    void deleteById(UUID commentId);
}
