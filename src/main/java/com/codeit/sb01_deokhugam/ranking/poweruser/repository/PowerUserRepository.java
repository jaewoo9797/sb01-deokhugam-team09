package com.codeit.sb01_deokhugam.ranking.poweruser.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeit.sb01_deokhugam.global.enumType.Period;
import com.codeit.sb01_deokhugam.ranking.poweruser.entity.PowerUser;

//todo 리뷰스코어썸 값 변경
public interface PowerUserRepository extends JpaRepository<PowerUser, UUID> {
	@Query(value = """
			SELECT
				:period,
				ROW_NUMBER() OVER (
					ORDER BY score DESC, user_created_at ASC
				) AS rank,
				score,
				review_score_sum,
				like_count,
				comment_count,
				user_id,
				nickname
			FROM (
				SELECT
					'WEEKLY' AS period,
					(COALESCE(SUM(r.like_count), 0) * 0.5
					 + COUNT(DISTINCT l.id) * 0.2
					 + COUNT(DISTINCT c.id) * 0.3) AS score,
					COALESCE(SUM(r.like_count), 0) AS review_score_sum, -- 여기 변경해야 함!!!!!!!!!!!
					COALESCE(SUM(r.like_count), 0) AS like_count,
					COUNT(DISTINCT c.id) AS comment_count,
					u.id AS user_id,
					u.nickname,
					u.created_at as user_created_at
				FROM users u
				LEFT JOIN reviews r
					ON r.user_id = u.id AND r.created_at BETWEEN :start AND :end
				LEFT JOIN comments c
					ON c.user_id = u.id AND c.created_at BETWEEN :start AND :end
				LEFT JOIN review_likes l
					ON l.user_id = u.id AND l.created_at BETWEEN :start AND :end
				GROUP BY u.id, u.nickname, u.created_at
			) AS ranked_users
			ORDER BY rank;
		""", nativeQuery = true)
	List<PowerUser> calculatePowerUserRank(
		@Param("start") Instant start,
		@Param("end") Instant end,
		@Param("period") Period period
	);

	long countByPeriod(Period period);
}
