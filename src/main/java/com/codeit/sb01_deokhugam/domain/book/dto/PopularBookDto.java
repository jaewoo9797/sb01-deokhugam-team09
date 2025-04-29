package com.codeit.sb01_deokhugam.domain.book.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.codeit.sb01_deokhugam.global.enumType.Period;

public record PopularBookDto(
	UUID id,
	UUID bookId,
	String title,
	String author,
	String thumbnailUrl,
	Period period,
	Integer rank,
	Double score,
	Integer reviewCount,
	BigDecimal rating,
	Instant createdAt
) {
}
