package com.codeit.sb01_deokhugam.domain.review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codeit.sb01_deokhugam.domain.review.dto.ReviewLikeDto;
import com.codeit.sb01_deokhugam.domain.review.entity.ReviewLike;

@Mapper(componentModel = "spring")
public interface ReviewLikeMapper {

	@Mapping(target = "reviewId", source = "reviewLike.reviewId")
	@Mapping(target = "userId", source = "reviewLike.userId")
	@Mapping(target = "liked", source = "liked")
	ReviewLikeDto toDto(ReviewLike reviewLike, boolean liked);
}
