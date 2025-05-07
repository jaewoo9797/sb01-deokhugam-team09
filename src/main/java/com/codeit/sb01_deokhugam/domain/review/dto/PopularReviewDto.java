package com.codeit.sb01_deokhugam.domain.review.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.codeit.sb01_deokhugam.global.enumType.Period;

public record PopularReviewDto(
	UUID id,
	UUID reviewId,
	UUID bookId,
	String bookTitle,
	String bookThumbnailUrl,
	UUID userId,
	String userNickname,
	BigDecimal reviewRating,
	Period period,
	int rank,
	BigDecimal score,
	int likeCount,
	int commentCount,
	Instant createdAt
) {
}
