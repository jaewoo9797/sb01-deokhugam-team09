package com.codeit.sb01_deokhugam.domain.review.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateRequest(
	@NotNull(message = "bookId는 필수입니다")
	UUID bookId,

	@NotNull(message = "userId는 필수입니다")
	UUID userId,

	@NotBlank(message = "content는 빈 값일 수 없습니다")
	String content,

	@NotNull(message = "rating은 필수입니다")
	@DecimalMin(value = "0.0", inclusive = true, message = "rating은 최소 0.0이어야 합니다")
	@DecimalMax(value = "5.0", inclusive = true, message = "rating은 최대 5.0이어야 합니다")
	BigDecimal rating
) {

}
