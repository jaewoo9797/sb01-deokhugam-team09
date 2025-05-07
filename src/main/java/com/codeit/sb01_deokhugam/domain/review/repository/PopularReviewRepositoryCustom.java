package com.codeit.sb01_deokhugam.domain.review.repository;

import java.time.Instant;
import java.util.List;

import com.codeit.sb01_deokhugam.domain.review.entity.ReviewRanking;
import com.codeit.sb01_deokhugam.global.enumType.Period;

public interface PopularReviewRepositoryCustom {
	/**
	 * 지정된 기간(period)에 대해, 커서(cursor)/after 이후부터
	 * rank 오름차순으로 limit 개수만큼 가져옵니다.
	 */
	List<ReviewRanking> findByPeriodWithCursor(
		Period period, String cursor, Instant after, int limit);
}
