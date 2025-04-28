package com.codeit.sb01_deokhugam.domain.review.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReviewDto(
	UUID id,
	UUID bookId,
	String bookTitle,
	String bookThumbnailUrl,
	UUID userId,
	String userNickname,
	String content,
	BigDecimal rating,
	int likeCount,
	int commentCount,
	boolean likedByMe,
	Instant createdAt,
	Instant updatedAt
) {

}
