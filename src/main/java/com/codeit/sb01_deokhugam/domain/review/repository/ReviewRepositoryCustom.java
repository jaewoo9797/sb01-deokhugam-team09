package com.codeit.sb01_deokhugam.domain.review.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.codeit.sb01_deokhugam.domain.review.entity.Review;

public interface ReviewRepositoryCustom {

	/**
	 * 논리적 삭제되지 않은 리뷰 조회
	 */
	Optional<Review> findByIdNotDeleted(UUID id);

	/**
	 * 커서 기반 리뷰 목록 검색
	 */
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

	/**
	 * 전체 개수 조회 (필터만 적용)
	 */
	long countByFilter(
		UUID filterUserId,
		UUID filterBookId,
		String keyword
	);
}
