package com.codeit.sb01_deokhugam.ranking.poweruser.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.codeit.sb01_deokhugam.domain.base.BaseEntity;
import com.codeit.sb01_deokhugam.global.enumType.Period;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_rankings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PowerUser extends BaseEntity {

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(name = "period", nullable = false)
	Period period;

	@Column(name = "rank", nullable = false)
	int rank;

	@Column(name = "score", precision = 2, scale = 1)
	BigDecimal score;

	@Column(name = "review_score_sum", precision = 2, scale = 1)
	BigDecimal reviewScoreSum;

	@Column(name = "like_count", nullable = false)
	int likeCount;

	@Column(name = "comment_count", nullable = false)
	int commentCount;
}
