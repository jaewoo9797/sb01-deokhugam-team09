package com.codeit.sb01_deokhugam.domain.review.dto;

import java.time.Instant;
import java.util.List;

public record CursorPageResponsePopularReviewDto(
	List<PopularReviewDto> content,
	String nextCursor,
	Instant nextAfter,
	int size,
	boolean hasNext,
	long totalElements
) {
}
