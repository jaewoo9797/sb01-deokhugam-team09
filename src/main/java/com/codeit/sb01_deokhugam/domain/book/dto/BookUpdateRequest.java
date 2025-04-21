package com.codeit.sb01_deokhugam.domain.book.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BookUpdateRequest (
	@NotNull(message = "ID는 반드시 입력되어야 합니다.")
	String id,

	@NotBlank(message = "제목은 반드시 입력되어야 합니다.")
	@Size(max = 255, message = "제목은 255자 이내로 입력해야 합니다.")
	String title,

	@NotBlank(message = "저자는 반드시 입력되어야 합니다.")
	@Size(max = 100, message = "저자는 100자 이내로 입력해야 합니다.")
	String author,

	@NotBlank(message = "설명은 반드시 입력되어야 합니다.")
	@Size(max = 500, message = "설명은 500자 이내로 입력해야 합니다.")
	String description,

	@NotBlank(message = "출판사는 반드시 입력되어야 합니다.")
	@Size(max = 100, message = "출판사는 100자 이내로 입력해야 합니다.")
	String publisher,

	@NotBlank(message = "ISBN은 반드시 입력되어야 합니다.")
	@Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "유효한 ISBN을 입력해 주세요.")
	String isbn,

	@NotBlank(message = "Thumbnail URL은 반드시 입력되어야 합니다.")
	@Pattern(regexp = "^(https?|ftp)://[^\s/$.?#].[^\s]*$", message = "유효한 URL 형식이어야 합니다.")
	String thumbnailUrl,

	@NotNull(message = "평점은 반드시 입력되어야 합니다.")
	@DecimalMin(value = "0.0", inclusive = false, message = "평점은 0보다 커야 합니다.")
	@DecimalMin(value = "5.0", inclusive = true, message = "평점은 5.0 이하여야 합니다.")
	BigDecimal rating,

	@NotNull(message = "리뷰수는 반드시 입력되어야 합니다.")
	int reviewCount,

	@NotNull(message = "출판일은 반드시 입력되어야 합니다.")
	LocalDate publishedDate,

	@NotNull(message = "Created at은 반드시 입력되어야 합니다.")
	Instant createdAt,

	@NotNull(message = "Updated at은 반드시 입력되어야 합니다.")
	Instant updatedAt
) {

}
