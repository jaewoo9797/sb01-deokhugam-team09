package com.codeit.sb01_deokhugam.domain.review.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.codeit.sb01_deokhugam.domain.review.entity.QReviewRanking;
import com.codeit.sb01_deokhugam.domain.review.entity.ReviewRanking;
import com.codeit.sb01_deokhugam.global.enumType.Period;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PopularReviewRepositoryImpl implements PopularReviewRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final QReviewRanking qRanking = QReviewRanking.reviewRanking;

	@Override
	public List<ReviewRanking> findByPeriodWithCursor(
		Period period, String cursor, Instant after, int limit) {

		BooleanBuilder where = new BooleanBuilder()
			.and(qRanking.period.eq(period));

		// 커서 조건: rank > cursorRank OR (rank == cursorRank AND createdAt > after)
		if (cursor != null && after != null) {
			int cursorRank = Integer.parseInt(cursor);
			where.and(new BooleanBuilder()
				.or(qRanking.rank.gt(cursorRank))
				.or(qRanking.rank.eq(cursorRank).and(qRanking.createdAt.gt(after))));
		}

		OrderSpecifier<Integer> orderByRank = qRanking.rank.asc();
		OrderSpecifier<Instant> orderByCreatedAt = qRanking.createdAt.asc();

		return queryFactory
			.selectFrom(qRanking)
			.where(where)
			.orderBy(orderByRank, orderByCreatedAt)
			.limit(limit)
			.fetch();
	}
}

