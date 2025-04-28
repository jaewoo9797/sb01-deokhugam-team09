package com.codeit.sb01_deokhugam.ranking.poweruser.dto.response;

import java.time.Instant;
import java.util.UUID;

public record PowerUserDto(
	UUID userId,
	String nickname,
	String period, //랭킹 조회시의 기간조건
	Instant createdAt,
	int rank,
	double score,
	double reviewScoreSum,
	int likeCount,
	int commentCount
) {
}
