package com.codeit.sb01_deokhugam.domain.review.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.sb01_deokhugam.domain.review.entity.ReviewLike;

public interface ReviewLikeRepository
	extends JpaRepository<ReviewLike, UUID> {

	Optional<ReviewLike> findByReviewIdAndUserId(UUID reviewId, UUID userId);

	// 토글용 삭제
	void deleteByReviewIdAndUserId(UUID reviewId, UUID userId);

	List<ReviewLike> findByCreatedAtBetween(Instant start, Instant end);

	void deleteByUserId(UUID userId);

}
