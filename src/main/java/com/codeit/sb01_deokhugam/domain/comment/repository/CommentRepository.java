package com.codeit.sb01_deokhugam.domain.comment.repository;

import com.codeit.sb01_deokhugam.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository <Comment, UUID> {

    // 댓글 목록 조회 (리뷰 id 기준)
    List<Comment> findByReviewIdAndDeletedFalseOrderByCreatedAtAsc(UUID reviewId);

    // 삭제되지 않은 댓글을 리뷰 ID와 함께 조회
    List<Comment> findByReviewIdAndDeletedFalse(UUID reviewId);

    // 댓글 ID로 조회
    Optional<Comment> findByIdAndDeletedFalse(UUID commentId);

    // 리뷰 ID 기준으로 댓글 목록을 페이지네이션 처리하여 조회 (생성 시간 기준)
    List<Comment> findByReviewIdAndDeletedFalseOrderByCreatedAtAsc(UUID reviewId, String after);

    // 작성자가 본인인지 확인할 수 있도록
    Optional<Comment> findByIdAndUserIdAndDeletedFalse(UUID commentId, UUID userId);

    // 댓글 물리삭제
    void deleteByIdAndDeletedFalse(UUID commentId);
}
