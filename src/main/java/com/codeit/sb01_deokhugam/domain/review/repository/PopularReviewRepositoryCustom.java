package com.codeit.sb01_deokhugam.domain.review.repository;

import java.time.Instant;
import java.util.List;

import com.codeit.sb01_deokhugam.domain.review.entity.ReviewRanking;
import com.codeit.sb01_deokhugam.global.enumType.Period;

public interface PopularReviewRepositoryCustom {

	List<ReviewRanking> findByPeriodWithCursor(
		Period period, String cursor, Instant after, int limit);
}
