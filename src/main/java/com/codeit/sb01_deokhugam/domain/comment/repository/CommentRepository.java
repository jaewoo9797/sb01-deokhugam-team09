package com.codeit.sb01_deokhugam.domain.comment.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.sb01_deokhugam.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID>, CommentRepositoryCustom {

	Optional<Comment> findByIdAndDeletedFalse(UUID commentId);

	Optional<Comment> findByIdAndUserIdAndDeletedFalse(UUID commentId, UUID userId);

	long countByReview_Id(UUID reviewId);

	List<Comment> findByCreatedAtBetweenAndDeletedFalse(Instant start, Instant end);

	// 물리 삭제
	void deleteById(UUID commentId);

	void deleteByUserId(UUID pathId);
}
