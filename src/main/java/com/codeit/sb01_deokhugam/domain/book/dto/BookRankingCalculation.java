package com.codeit.sb01_deokhugam.domain.book.dto;

import java.math.BigDecimal;

//도서 배치 연산에서 해당 기간의 도서 스코어를 담는 dto
public record BookRankingCalculation(
	BigDecimal score,
	int reviewCount,
	BigDecimal avgRating
) {
}
