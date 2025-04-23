package com.codeit.sb01_deokhugam.domain.review.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record ReviewLikeDto(

	@NotBlank(message = "reviewId는 필수입니다")
	UUID reviewId,

	@NotBlank(message = "userId는 필수입니다")
	UUID userId,

	boolean liked

) {
}
