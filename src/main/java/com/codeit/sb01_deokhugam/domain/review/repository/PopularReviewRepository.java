package com.codeit.sb01_deokhugam.domain.review.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.sb01_deokhugam.domain.review.entity.ReviewRanking;
import com.codeit.sb01_deokhugam.global.enumType.Period;

public interface PopularReviewRepository
	extends JpaRepository<ReviewRanking, UUID>, PopularReviewRepositoryCustom {

	long countByPeriod(Period period);

	void deleteByPeriod(Period period);
}
