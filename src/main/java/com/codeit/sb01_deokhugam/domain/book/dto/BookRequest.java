package com.codeit.sb01_deokhugam.domain.book.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BookRequest(
	@NotBlank(message = "저자는 반드시 입력되어야 합니다.")
	@Size(max = 255, message = "저자는 255자 이내로 입력해야 합니다.")
	String author,

	@NotBlank(message = "설명은 반드시 입력되어야 합니다.")
	@Size(max = 1000, message = "설명은 1000자 이내로 입력해야 합니다.")
	String description,

	@NotBlank(message = "ISBN은 반드시 입력되어야 합니다.")
	@Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "유효한 ISBN을 입력해 주세요.")
	String isbn,

	@NotNull(message = "출판일은 반드시 입력되어야 합니다.")
	LocalDate publishedDate,

	@NotBlank(message = "출판사는 반드시 입력되어야 합니다.")
	@Size(max = 255, message = "출판사는 255자 이내로 입력해야 합니다.")
	String publisher,

	@NotBlank(message = "Thumbnail URL은 반드시 입력되어야 합니다.")
	@Pattern(regexp = "^(https?|ftp)://[^\s/$.?#].[^\s]*$", message = "유효한 URL 형식이어야 합니다.")
	String thumbnailUrl,

	@NotBlank(message = "제목은 반드시 입력되어야 합니다.")
	@Size(max = 255, message = "제목은 255자 이내로 입력해야 합니다.")
	String title

) {

}
