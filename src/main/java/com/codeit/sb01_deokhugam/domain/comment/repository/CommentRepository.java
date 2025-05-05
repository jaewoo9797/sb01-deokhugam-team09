package com.codeit.sb01_deokhugam.domain.comment.repository;

import com.codeit.sb01_deokhugam.domain.comment.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository <Comment, UUID> {

    // 삭제되지 않은 댓글 목록 조회 (리뷰 ID 기준, 생성일 오름차순 정렬)
    List<Comment> findByReviewIdAndDeletedFalseOrderByCreatedAtAsc(UUID reviewId);

    // 삭제되지 않은 댓글을 리뷰 ID로 조회
    List<Comment> findByReviewIdAndDeletedFalse(UUID reviewId);

    // 삭제되지 않은 댓글을 ID로 조회
    Optional<Comment> findByIdAndDeletedFalse(UUID commentId);

    // 특정 시간 이후 생성된 댓글만 조회
    // List<Comment> findByReviewIdAndDeletedFalseAndCreatedAtAfterOrderByCreatedAtAsc(UUID reviewId, Instant after);

    List<Comment> findByReviewIdAndDeletedFalseAndCreatedAtAfterOrderByCreatedAt(UUID reviewId, Instant after, Sort sort);

    List<Comment> findByReviewIdAndDeletedFalse(UUID reviewId, Sort sort);

    // 작성자가 본인인지 확인할 수 있도록
    Optional<Comment> findByIdAndUserIdAndDeletedFalse(UUID commentId, UUID userId);

    // 물리 삭제
    void deleteById(UUID commentId);
}
