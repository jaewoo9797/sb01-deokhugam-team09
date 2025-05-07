package com.codeit.sb01_deokhugam.domain.comment.repository;
import com.codeit.sb01_deokhugam.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository <Comment, UUID>, CommentRepositoryCustom {

    Optional<Comment> findByIdAndDeletedFalse(UUID commentId);

    Optional<Comment> findByIdAndUserIdAndDeletedFalse(UUID commentId, UUID userId);

    long countByReviewId(UUID reviewId);

    // 물리 삭제
    void deleteById(UUID commentId);
}
