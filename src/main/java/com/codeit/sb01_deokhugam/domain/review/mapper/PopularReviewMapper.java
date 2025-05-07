package com.codeit.sb01_deokhugam.domain.review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codeit.sb01_deokhugam.domain.review.dto.PopularReviewDto;
import com.codeit.sb01_deokhugam.domain.review.entity.ReviewRanking;

@Mapper(componentModel = "spring")
public interface PopularReviewMapper {

	@Mapping(target = "reviewId", source = "review.id")
	PopularReviewDto toDto(ReviewRanking ranking);
}
