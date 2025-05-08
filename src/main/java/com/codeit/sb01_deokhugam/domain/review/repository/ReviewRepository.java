package com.codeit.sb01_deokhugam.domain.review.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codeit.sb01_deokhugam.domain.review.entity.Review;

@Repository
public interface ReviewRepository
	extends JpaRepository<Review, UUID>, ReviewRepositoryCustom {

	List<Review> findByCreatedAtBetween(Instant start, Instant end);

	Optional<Review> findByAuthorIdAndBookIdAndDeletedFalse(
		@Param("authorId") UUID authorId,
		@Param("bookId") UUID bookId
	);

	@Modifying
	@Query("UPDATE Review r SET r.likeCount = r.likeCount + 1 WHERE r.id = :id")
	void incrementLikeCount(@Param("id") UUID id);

	@Modifying
	@Query("""
		UPDATE Review r
		SET r.likeCount = CASE WHEN r.likeCount > 0 THEN r.likeCount - 1 ELSE 0 END
		WHERE r.id = :id
		""")
	void decrementLikeCount(@Param("id") UUID id);

	void deleteByUserId(UUID pathId);
}
