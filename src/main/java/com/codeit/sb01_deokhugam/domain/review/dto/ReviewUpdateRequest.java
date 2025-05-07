package com.codeit.sb01_deokhugam.domain.review.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewUpdateRequest(

	@NotBlank(message = "content는 빈 값일 수 없습니다")
	String content,

	@NotNull(message = "rating은 필수입니다")
	@Min(value = 0, message = "rating은 최소 0이어야 합니다")
	@Max(value = 5, message = "rating은 최대 5이어야 합니다")
	BigDecimal rating
) {
}
