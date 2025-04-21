package com.codeit.sb01_deokhugam.domain.book.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record BookDto(
	String author,
	Instant createdAt,
	String description,
	UUID id,
	String isbn,
	LocalDate publishedDate,
	String publisher,
	BigDecimal rating,
	Integer reviewCount,
	String thumbnailUrl,
	String title,
	Instant updatedAt
) {
}
