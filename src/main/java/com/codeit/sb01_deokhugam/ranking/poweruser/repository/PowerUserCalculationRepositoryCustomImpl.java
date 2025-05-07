package com.codeit.sb01_deokhugam.ranking.poweruser.repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.codeit.sb01_deokhugam.global.enumType.Period;
import com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.PowerUserDto;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PowerUserCalculationRepositoryCustomImpl implements PowerUserCalculationRepositoryCustom {
	private final EntityManager em;

	@Override
	public List<PowerUserDto> findUserActivityBetween(Map.Entry<Instant, Instant> StartAndEndTime) {
		Instant start = StartAndEndTime.getKey();
		Instant end = StartAndEndTime.getValue();

		String jpql = """
			    SELECT new com.codeit.sb01_deokhugam.ranking.poweruser.dto.response.PowerUserDto(
			        u.id,
			        u.nickname,
			        :period,
			        CURRENT_TIMESTAMP,
			        0,
			        (COALESCE(SUM(r.likeCount), 0) * 0.5 + COUNT(DISTINCT l) * 0.2 + COUNT(DISTINCT c) * 0.3),
			        COALESCE(SUM(r.likeCount), 0),
			        COUNT(DISTINCT l),
			        COUNT(DISTINCT c)
			    )
			    FROM User u
			    LEFT JOIN Review r ON r.author = u AND r.createdAt BETWEEN :start AND :end
			    LEFT JOIN Comment c ON c.userId = u.id AND c.createdAt BETWEEN :start AND :end
			    LEFT JOIN ReviewLike l ON l.userId = u AND l.createdAt BETWEEN :start AND :end
			    GROUP BY u.id, u.nickname
			    ORDER BY (COALESCE(SUM(r.likeCount), 0) * 0.5 + COUNT(DISTINCT l) * 0.2 + COUNT(DISTINCT c) * 0.3) DESC
			""";

		return em.createQuery(jpql, PowerUserDto.class)
			.setParameter("start", start)
			.setParameter("end", end)
			.setParameter("period", Period.DAILY)
			.getResultList();
	}
}
