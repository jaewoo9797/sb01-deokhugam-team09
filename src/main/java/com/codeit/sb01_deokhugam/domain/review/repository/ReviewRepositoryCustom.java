package com.codeit.sb01_deokhugam.domain.review.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.codeit.sb01_deokhugam.domain.review.entity.Review;

public interface ReviewRepositoryCustom {
	Optional<Review> findByIdNotDeleted(UUID id);

	List<Review> findListByCursor(
		UUID filterUserId,
		UUID filterBookId,
		String keyword,
		Instant after,
		String cursor,
		String orderBy,
		String direction,
		int limit
	);

	long countByFilter(UUID filterUserId, UUID filterBookId, String keyword);
}
